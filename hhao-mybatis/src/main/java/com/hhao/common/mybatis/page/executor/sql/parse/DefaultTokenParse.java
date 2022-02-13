/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.mybatis.page.executor.sql.parse;

import com.hhao.common.mybatis.page.executor.sql.token.FragmentToken;
import com.hhao.common.mybatis.page.executor.sql.token.ParamToken;
import com.hhao.common.mybatis.page.executor.sql.token.SelectToken;
import com.hhao.common.mybatis.page.executor.sql.token.Token;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * mysql标准的sql语句进行解析
 * <pre>{@code
 * SELECT
 *     [ALL | DISTINCT | DISTINCTROW ]
 *     [HIGH_PRIORITY]
 *     [STRAIGHT_JOIN]
 *     [SQL_SMALL_RESULT] [SQL_BIG_RESULT] [SQL_BUFFER_RESULT]
 *     [SQL_NO_CACHE] [SQL_CALC_FOUND_ROWS]
 *     select_expr [, select_expr] ...
 *     [into_option]
 *     [FROM table_references
 *       [PARTITION partition_list]]
 *     [WHERE where_condition]
 *     [GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]]
 *     [HAVING where_condition]
 *     [WINDOW window_name AS (window_spec)
 *         [, window_name AS (window_spec)] ...]
 *     [ORDER BY {col_name | expr | position}
 *       [ASC | DESC], ... [WITH ROLLUP]]
 *     [LIMIT {[offset,] row_count | row_count OFFSET offset}]
 *     [into_option]
 *     [FOR {UPDATE | SHARE}
 *         [OF tbl_name [, tbl_name] ...]
 *         [NOWAIT | SKIP LOCKED]
 *       | LOCK IN SHARE MODE]
 *     [into_option]
 *
 * into_option: {
 *     INTO OUTFILE 'file_name'
 *         [CHARACTER SET charset_name]
 *         export_options
 *   | INTO DUMPFILE 'file_name'
 *   | INTO var_name [, var_name] ...
 * }
 * }**
 * </pre>
 *
 * @author Wang
 * @since 2021 /11/18 19:46
 */
public class DefaultTokenParse implements TokenParse {
    /**
     * The Logger.
     */
    protected final Log logger = LogFactory.getLog(this.getClass());
    /**
     * token清除规则
     */
    protected ParseRule parseRule = new ParseRule();
    /**
     * 完整sql语句解析后的tokens
     */
    protected List<Token> tokens = new ArrayList<>();
    /**
     * 清除的参数
     */
    protected List<Token> paramTokens = new ArrayList<>();
    /**
     * 语句中是否包含union
     */
    protected boolean union = false;
    /**
     * The Contain limit.
     */
    protected boolean containLimit=false;

    /**
     * The Token infos.
     */
    protected List<TokenInfo> tokenInfos = new ArrayList<>();
    /**
     * 是否跳过order by 子句
     */
    protected boolean skipOrderBy = false;
    /**
     * 是否跳过limit子句
     */
    protected boolean skipLimit = false;
    /**
     * 是否跳过offset子句
     */
    protected boolean skipOffset = false;
    /**
     * 是否跳过select列
     */
    protected boolean skipRootSelectItems = false;
    /**
     * ?参数的序列位置
     */
    protected int paramIndex = 0;

    /**
     * The Parentheses stack.
     */
    protected Stack<Token> parenthesesStack = new Stack<>();
    /**
     * The Select stack.
     */
    protected Stack<Token> selectStack = new Stack<>();
    /**
     * The From stack.
     */
    protected Stack<Token> fromStack = new Stack<>();
    /**
     * The Where stack.
     */
    protected Stack<Token> whereStack = new Stack<>();
    /**
     * The Group by stack.
     */
    protected Stack<Token> groupByStack = new Stack<>();
    /**
     * The Having stack.
     */
    protected Stack<Token> havingStack = new Stack<>();
    /**
     * The Order by stack.
     */
    protected Stack<Token> orderByStack = new Stack<>();
    /**
     * The Limit stack.
     */
    protected Stack<Token> limitStack = new Stack<>();
    /**
     * The Offset stack.
     */
    protected Stack<Token> offsetStack = new Stack<>();

    /**
     * The Union stack.
     */
    protected Stack<Token> unionStack = new Stack<>();

    /**
     * The Current fragment stack.
     */
    protected Stack<Token> currentFragmentStack = new Stack<>();

    /**
     * Instantiates a new Parse result.
     */
    public DefaultTokenParse() {

    }

    /**
     * Instantiates a new Parse result.
     *
     * @param parseRule the parse rule
     */
    public DefaultTokenParse(ParseRule parseRule) {
        this.parseRule = parseRule;
    }

    /**
     * Add.
     *
     * @param token the token
     */
    @Override
    public void parseToken(Token token) {
        token = parse(token);

        //判断一条sql语句是否已完成
        if (checkFinishOneQuerySql(token)){
            finish();
        }else {
            tokens.add(token);
        }
    }

    /**
     * Parse token token.
     *
     * @param token the token
     * @return the token
     */
    protected Token parse(Token token) {
        //参数占位符处理
        token = filterParam(token);
        //左、右小括号处理
        token = filterParentheses(token);
        //select处理
        token = filterSelect(token);
        token = filterFrom(token);
        token = filterWhere(token);
        token = filterGroupBy(token);
        token = filterHaving(token);
        token = filterOrderBy(token);
        token = filterLimit(token);
        token = filterOffset(token);
        token = filterUnion(token);

        //公共设置处理
        token.setIndex(tokens.size());
        token.setFragmentToken(this.getCurrentFragment());
        filterClean(token);

        filterOther(token);

        return token;
    }


    /**
     * 如果为true,保留
     * 如果为false,去除
     *
     * @param token the token
     * @return boolean
     */
    protected boolean filterClean(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();
        if (SqlKeyword.ORDER.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            token.setClean(skipOrderBy);
        } else if (SqlKeyword.LIMIT.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            token.setClean(skipLimit);
        } else if (SqlKeyword.OFFSET.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            token.setClean(skipOffset);
        } else if (getRootSelectToken() != null && getRootSelectToken().getFromToken() == null) {
            token.setClean(skipRootSelectItems);
        } else {
            token.setClean(false);
        }
        return token.isClean();
    }

    /**
     * sql语句中参数?占位符处理
     *
     * @param token the token
     * @return the token
     */
    protected Token filterParam(Token token) {
        if (SqlKeyword.PARAM.getValue().equalsIgnoreCase(token.getValue())) {
            ParamToken paramToken = ParamToken.of(token);
            paramToken.setParamIndex(paramIndex);
            paramIndex++;
            token = paramToken;
            paramTokens.add(paramToken);
        }
        return token;
    }

    /**
     * 左、右小括号处理
     *
     * @param token the token
     * @return the token
     */
    protected Token filterParentheses(Token token) {
        FragmentToken leftToken = null;
        FragmentToken rightToken = null;
        if (SqlKeyword.LEFT_PARENTHESIS.getValue().equalsIgnoreCase(token.getValue())) {
            leftToken = FragmentToken.of(token);
            parenthesesStack.push(leftToken);
            leftToken.setBeginToken(leftToken);
            token = leftToken;
        } else if (SqlKeyword.RIGHT_PARENTHESIS.getValue().equalsIgnoreCase(token.getValue())) {
            rightToken = FragmentToken.of(token);
            leftToken = (FragmentToken) parenthesesStack.pop();
            leftToken.setEndToken(rightToken);
            rightToken.setBeginToken(leftToken);
            rightToken.setEndToken(rightToken);

            Token leftNext = this.getNextToken(leftToken);
            if (SqlKeyword.SELECT.getValue().equalsIgnoreCase(leftNext.getValue())) {
                currentFragmentStack.pop();
            }

            token = rightToken;
        }
        return token;
    }


    /**
     * The Select finish tag.
     */
    protected String[] selectFinishTag = new String[]{
            SqlKeyword.UNION.getValue()
    };

    /**
     * Select fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean selectFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (matchAny(token, selectFinishTag)) {
            return true;
        }
        if (SqlKeyword.RIGHT_PARENTHESIS.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken rightToken = (FragmentToken) token;
            Token nexToken=this.getNextToken(rightToken.getBeginToken());
            if (nexToken!=null && SqlKeyword.SELECT.getValue().equalsIgnoreCase(nexToken.getValue())){
                return true;
            }
        }
        return false;
    }

    /**
     * Filter select token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterSelect(Token token) {
        if (SqlKeyword.SELECT.getValue().equalsIgnoreCase(token.getValue())) {
            SelectToken selectToken = SelectToken.of(token);
            selectToken.setBeginToken(selectToken);

            selectStack.push(selectToken);
            currentFragmentStack.push(selectToken);

            if (parseRule.isCleanRootSelectItems() && selectStack.size() == 1) {
                skipRootSelectItems = true;
            }
            token = selectToken;
        } else if (selectFragmentFinish(token)) {
            SelectToken selectToken = (SelectToken) selectStack.pop();
            currentFragmentStack.pop();

            selectToken.setEndToken(tokens.get(tokens.size() - 1));
        }
        return token;
    }

    /**
     * The From finish tag.
     */
    protected String[] fromFinishTag = new String[]{
            SqlKeyword.WHERE.getValue(),
            SqlKeyword.GROUP.getValue(),
            SqlKeyword.ORDER.getValue(),
            SqlKeyword.LIMIT.getValue(),
            SqlKeyword.OFFSET.getValue(),
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * From fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean fromFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.FROM.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, fromFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter from token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterFrom(Token token) {
        if (SqlKeyword.FROM.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken fromToken = FragmentToken.of(token);
            fromStack.push(fromToken);
            currentFragmentStack.push(fromToken);

            fromToken.setBeginToken(fromToken);

            getCurrentSelectToken().setFromToken(fromToken);

            if (selectStack.size() == 1) {
                skipRootSelectItems = false;
            }
            token = fromToken;
        } else if (fromFragmentFinish(token)) {
            FragmentToken fromToken = (FragmentToken) fromStack.pop();
            currentFragmentStack.pop();

            fromToken.setEndToken(tokens.get(tokens.size() - 1));
        }
        return token;
    }

    /**
     * The Where finish tag.
     */
    protected String[] whereFinishTag = new String[]{
            SqlKeyword.GROUP.getValue(),
            SqlKeyword.ORDER.getValue(),
            SqlKeyword.LIMIT.getValue(),
            SqlKeyword.OFFSET.getValue(),
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * Where fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean whereFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.WHERE.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, whereFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter where token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterWhere(Token token) {
        if (SqlKeyword.WHERE.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken whereToken = FragmentToken.of(token);

            whereStack.push(whereToken);
            currentFragmentStack.push(whereToken);

            whereToken.setBeginToken(whereToken);

            getCurrentSelectToken().setWhereToken(whereToken);

            token = whereToken;
        } else if (whereFragmentFinish(token)) {
            FragmentToken whereToken = (FragmentToken) whereStack.pop();
            currentFragmentStack.pop();

            whereToken.setEndToken(tokens.get(tokens.size() - 1));
        }
        return token;
    }

    /**
     * The Group by finish tag.
     */
    protected String[] groupByFinishTag = new String[]{
            SqlKeyword.HAVING.getValue(),
            SqlKeyword.ORDER.getValue(),
            SqlKeyword.LIMIT.getValue(),
            SqlKeyword.OFFSET.getValue(),
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * Group by fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean groupByFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.GROUP.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, groupByFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter group by token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterGroupBy(Token token) {
        if (SqlKeyword.GROUP.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken groupByToken = FragmentToken.of(token);
            groupByStack.push(groupByToken);
            currentFragmentStack.push(groupByToken);

            groupByToken.setBeginToken(groupByToken);

            getCurrentSelectToken().setGroupByToken(groupByToken);

            token = groupByToken;
        } else if (groupByFragmentFinish(token)) {
            FragmentToken groupByToken = (FragmentToken) groupByStack.pop();
            currentFragmentStack.pop();

            groupByToken.setEndToken(tokens.get(tokens.size() - 1));
        }
        return token;
    }

    /**
     * The Having finish tag.
     */
    protected String[] havingFinishTag = new String[]{
            SqlKeyword.ORDER.getValue(),
            SqlKeyword.LIMIT.getValue(),
            SqlKeyword.OFFSET.getValue(),
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * Having fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean havingFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.HAVING.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, havingFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter having token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterHaving(Token token) {
        if (SqlKeyword.HAVING.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken havingToken = FragmentToken.of(token);
            havingStack.push(havingToken);
            currentFragmentStack.push(havingToken);

            havingToken.setBeginToken(havingToken);

            getCurrentSelectToken().setHavingToken(havingToken);
            token = havingToken;
        } else if (havingFragmentFinish(token)) {
            FragmentToken havingToken = (FragmentToken) havingStack.pop();
            currentFragmentStack.pop();

            havingToken.setEndToken(tokens.get(tokens.size() - 1));
        }
        return token;
    }

    /**
     * The Order by finish tag.
     */
    protected String[] orderByFinishTag = new String[]{
            SqlKeyword.LIMIT.getValue(),
            SqlKeyword.OFFSET.getValue(),
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * Order by fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean orderByFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.ORDER.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, orderByFinishTag)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Filter order by token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterOrderBy(Token token) {
        if (SqlKeyword.ORDER.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken orderByToken = FragmentToken.of(token);
            orderByStack.push(orderByToken);
            currentFragmentStack.push(orderByToken);

            orderByToken.setBeginToken(orderByToken);

            if (parseRule.isCleanOrderBy()) {
                skipOrderBy = true;
            }

            getCurrentSelectToken().setOrderByToken(orderByToken);
            token = orderByToken;
        } else if (orderByFragmentFinish(token)) {
            FragmentToken orderByToken = (FragmentToken) orderByStack.pop();
            currentFragmentStack.pop();

            orderByToken.setEndToken(tokens.get(tokens.size() - 1));
            skipOrderBy = false;
        }
        return token;
    }

    /**
     * The Limit finish tag.
     */
    protected String[] limitFinishTag = new String[]{
            SqlKeyword.OFFSET.getValue(),
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * Limit fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean limitFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.LIMIT.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, limitFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter limit token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterLimit(Token token) {
        if (SqlKeyword.LIMIT.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken limitToken = FragmentToken.of(token);
            limitStack.push(limitToken);
            currentFragmentStack.push(limitToken);

            limitToken.setBeginToken(limitToken);

            if (parseRule.isCleanLimit()) {
                skipLimit = true;
            }

            getCurrentSelectToken().setLimitToken(limitToken);
            token = limitToken;
        } else if (limitFragmentFinish(token)) {
            FragmentToken limitToken = (FragmentToken) limitStack.pop();
            currentFragmentStack.pop();

            limitToken.setEndToken(tokens.get(tokens.size() - 1));
            skipLimit = false;
        }
        return token;
    }

    /**
     * The Offset finish tag.
     */
    protected String[] offsetFinishTag = new String[]{
            SqlKeyword.FOR.getValue(),
            SqlKeyword.UNION.getValue(),
            SqlKeyword.SEPARATOR.getValue()
    };

    /**
     * Offset fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean offsetFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.OFFSET.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, offsetFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter offset token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterOffset(Token token) {
        if (SqlKeyword.OFFSET.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken offsetToken = FragmentToken.of(token);
            offsetStack.push(offsetToken);
            currentFragmentStack.push(offsetToken);

            offsetToken.setBeginToken(offsetToken);

            if (parseRule.isCleanOffset()) {
                skipOffset = true;
            }

            getCurrentSelectToken().setOffsetToken(offsetToken);
            token = offsetToken;
        } else if (offsetFragmentFinish(token)) {
            FragmentToken offsetToken = (FragmentToken) offsetStack.pop();
            currentFragmentStack.pop();

            offsetToken.setEndToken(tokens.get(tokens.size() - 1));
            skipOffset = false;
        }
        return token;
    }

    /**
     * The Union finish tag.
     */
    protected String[] unionFinishTag = new String[]{
            SqlKeyword.ALL.getValue(),
            SqlKeyword.DISTINCT.getValue(),
            SqlKeyword.SELECT.getValue()
    };

    /**
     * Union fragment finish boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean unionFragmentFinish(Token token) {
        FragmentToken currentFragment = this.getCurrentFragment();

        if (currentFragment!=null && SqlKeyword.UNION.getValue().equalsIgnoreCase(currentFragment.getValue())) {
            if (matchAny(token, unionFinishTag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Filter union token.
     *
     * @param token the token
     * @return the token
     */
    protected Token filterUnion(Token token) {
        if (SqlKeyword.UNION.getValue().equalsIgnoreCase(token.getValue())) {
            FragmentToken unionToken = FragmentToken.of(token);
            unionStack.push(unionToken);
            currentFragmentStack.push(unionToken);

            unionToken.setBeginToken(unionToken);
            unionToken.setEndToken(unionToken);

            union = true;
            token = unionToken;
        } else if (unionFragmentFinish(token)) {
            FragmentToken unionToken = (FragmentToken) unionStack.pop();
            currentFragmentStack.pop();

            unionToken.setEndToken(tokens.get(tokens.size() - 1));
        }
        return token;
    }

    /**
     * Filter other.
     *
     * @param token the token
     */
    protected void filterOther(Token token){
        if (SqlKeyword.LIMIT.getValue().equalsIgnoreCase(token.getValue())){
            containLimit=true;
        }
    }

    /**
     * Check finish one query sql boolean.
     *
     * @param token the token
     * @return the boolean
     */
    protected boolean checkFinishOneQuerySql(Token token){
        if (SqlKeyword.SEPARATOR.getValue().equalsIgnoreCase(token.getValue())) {
            return true;
        }
        return false;
    }

    /**
     * 一条sql语句结束后的处理
     */
    protected void finish() {
        if (!selectStack.empty()) {
            SelectToken selectToken = (SelectToken) selectStack.pop();
            selectToken.setEndToken(tokens.get(tokens.size() - 1));

            tokenInfos.add(new DefaultTokenInfo(tokens, paramTokens,union,containLimit));
            tokens =new ArrayList<>();
            paramTokens =new ArrayList<>();
            union=false;
            containLimit=false;
        }
    }

    /**
     * Gets next.
     *
     * @param token the token
     * @return the next
     */
    protected Token getNextToken(Token token) {
        if (token.getIndex() > 0 && (token.getIndex() + 1) < tokens.size()) {
            return tokens.get(token.getIndex() + 1);
        }
        return null;
    }

    protected Token getLastToken() {
        if (tokens.size()>0) {
            return tokens.get(tokens.size()-1);
        }
        return null;
    }

    /**
     * 判断values中是否包含token
     *
     * @param token  the token
     * @param values the values
     * @return the boolean
     */
    protected boolean matchAny(Token token, String[] values) {
        for (String v : values) {
            if (v.equalsIgnoreCase(token.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get current fragment fragment token.
     *
     * @return the fragment token
     */
    protected FragmentToken getCurrentFragment() {
        if (currentFragmentStack.empty()) {
            return null;
        }
        return (FragmentToken) currentFragmentStack.peek();
    }

    /**
     * Get current select token select token.
     *
     * @return the select token
     */
    protected SelectToken getCurrentSelectToken() {
        if (selectStack.empty()) {
            return null;
        }
        return (SelectToken) selectStack.peek();
    }

    /**
     * Gets root select token.
     *
     * @return the root select token
     */
    protected SelectToken getRootSelectToken() {
        if (selectStack.empty()) {
            return null;
        }
        return (SelectToken) selectStack.get(0);
    }

    @Override
    public List<TokenInfo> getTokenInfos(){
        this.finish();
        return this.tokenInfos;
    }
}
