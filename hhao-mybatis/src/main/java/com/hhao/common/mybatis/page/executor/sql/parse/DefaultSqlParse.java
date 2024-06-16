/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.mybatis.page.executor.sql.parse;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.mybatis.page.executor.sql.token.SqlToken;
import com.hhao.common.mybatis.page.executor.sql.token.Token;

import java.util.List;

/**
 * 参考mysql的标准sql语法作解析
 *
 * @author Wang
 * @since 2021 /11/18 19:06
 */
public class DefaultSqlParse implements SqlParse {
    /**
     * The Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(DefaultSqlParse.class);

    private TokenParse tokenParse;

    /**
     * Instantiates a new Default sql parse.
     *
     * @param tokenParse the token parse
     */
    public DefaultSqlParse(TokenParse tokenParse){
        this.tokenParse=tokenParse;
    }

    /**
     * Instantiates a new Default sql parse.
     *
     * @param parseRule the parse rule
     */
    public DefaultSqlParse(ParseRule parseRule){
        this.tokenParse=new DefaultTokenParse(parseRule);
    }

    /**
     * Instantiates a new Default sql parse.
     */
    public DefaultSqlParse(){
        this.tokenParse=new DefaultTokenParse(new ParseRule());
    }

    @Override
    public List<TokenInfo> parseSql(String sql) {
        char [] originSql=sql.toCharArray();
        char c;
        StringBuffer tokenBuffer=new StringBuffer();
        //记录token在原始sql语句中的起始序列位置
        int begin=0;

        for(int i=0;i<originSql.length;i++){
            c=originSql[i];
            //遇到空格、换行时一个token的结束处理
            if (c=='\t' || c=='\n' || c=='\r' || c==' '){
                if (tokenBuffer.length()>0) {
                    tokenParse.parseToken(new SqlToken(tokenBuffer.toString()));
                    tokenBuffer=new StringBuffer();
                }
                begin=i+1;
            }else if(c==',' || c=='(' || c==')' || c==';' || c=='?') {
                //遇到,();?时一个token的结束处理
                if (tokenBuffer.length()>0) {
                    tokenParse.parseToken(new SqlToken(tokenBuffer.toString()));
                    tokenBuffer=new StringBuffer();
                    begin=i;
                }
                tokenParse.parseToken(new SqlToken(String.valueOf(c)));
                begin=i+1;
            }else{
                //说明token获取未结束
                tokenBuffer.append(c);
            }
        }
        //最后遗留token的处理
        if (tokenBuffer.length()>0) {
            tokenParse.parseToken(new SqlToken(tokenBuffer.toString()));
        }
        log.debug(tokenParse.getTokenInfos().toString());
        return tokenParse.getTokenInfos();
    }


    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String [] args){
        String sql1="select book1.id as id, book1.name as name, book1.price as price, book1.priceCurrencyCode as priceCurrencyCode, book1.publicDate as publicDate, book1.recordDateTime as recordDateTime, book1.type as type , bookType.id as bookType_id, bookType.name as bookType_name from book as book1 inner join( select id from book where recordDateTime between ? and ? order by id limit ? offset ? ) as book2 on book1.id = book2.id inner join `book-type` as bookType on book1.type=bookType.id order by recordDateTime ASC";
        String sql2="select sum(book1.id) as id from book as book1 inner join( select id from book where recordDateTime between ? and ? order by id limit ? offset ? ) as book2 on book1.id = book2.id inner join `book-type` as bookType on book1.type=bookType.id order by recordDateTime ASC";
        String sql3="select (select max(id) as m from book) as max from book as book1 inner join( select id from book where recordDateTime between ? and ? order by id limit ? offset ? ) as book2 on book1.id = book2.id inner join `book-type` as bookType on book1.type=bookType.id order by recordDateTime ASC";
        String sql4="select (select max(id) as m from book) as max,(select max(id) as m from book) as max from book as book1 inner join( select id from book where recordDateTime between ? and ? order by id limit ? offset ? ) as book2 on book1.id = book2.id inner join `book-type` as bookType on book1.type=bookType.id order by recordDateTime ASC";
        String sql5="select (select max(id) as m from book) id from book as book1 inner join( select id from book where recordDateTime between ? and ? order by id limit ? offset ? ) as book2 on book1.id = book2.id inner join `book-type` as bookType on book1.type=bookType.id where id=(select max(id) from book where pp=?) and name=? order by recordDateTime ASC";
        String sql6="select (select max(id) as m from book) id from book as book1 inner join( select id from book where recordDateTime between ? and ? order by id limit ? offset ? ) as book2 on book1.id = book2.id inner join `book-type` as bookType on book1.type=bookType.id where id=(select max(id) from book where pp=?) and name=? order by recordDateTime ASC union select id,name from book order by id;select count(*) from book limit ? offset ?";
        String sql7="select id from book union select id,name from book order by id";

        //String sql="?(( ?),人)  ,(k ,f011)?12, ?";
        List<TokenInfo> tokenInfos = new DefaultSqlParse().parseSql(sql6);
        for(TokenInfo tokenInfo :tokenInfos){
            for(Token token:tokenInfo.getTokens()) {
                System.out.println(token);
            }
            System.out.println(tokenInfo.getSql());
            System.out.println(tokenInfo.getCleanSql());
            System.out.println(tokenInfo.isUnion());
            System.out.println("=============================================");
        }
    }


}
