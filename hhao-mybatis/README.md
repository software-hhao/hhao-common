# HHAO Mybatis增强模块

增强Mybatis查询与分页处理。

# 主要的类

## PageExecutor：分页执行器接口

具体实现类：

MultiQueriesDynamicPageExecutor

* 多语句动态分页执行器。
* 适用于JDBC支持多语句执行的情况。
* 执行器对以下内容进行处理：
  1、对select语句进行分析，如果未分页，则加入分页，并对查询参数进行检查、补齐。
  2、对count语句进行分析，如果不存在，则自动生成，并对参数进行检查、删除或补齐。
* 可以自提供count语句，count语句与查询语句定义在同一块中。（推荐做法）
* 该执行器比较灵活，但受到不同数据库语言差异的影响，效率次于MultiQueriesStaticPageExecutor。

MultiQueriesStaticPageExecutor（推荐）

* 多语句静态分页执行器。
* 适用于JDBC支持多语句执行的情况。
* 由用户自己定义分页查询语句与count语句，执行器完成count语句的入参与织入。
* count语句与查询语句定义在同一块中。
* 该执行器不受数据库语言差异的影响，分页查询语句及count语句完全由用户提供，执行效率最高。

上述的两种执行器，需要JDBC支持多语名执行，例如，mysql jdbc应开启多语句执行：

```
jdbc:mysql://${server.mysql.ip}:6447/test?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&allowMultiQueries=true
```

SingleQueryDynamicPageExecutor（推荐）

* 单语句动态分页执行器。
* 适用于JDBC只支持单语句执行的情况。
* 执行器对以下内容进行处理。
  1、对select语句进行分析，如果未分页，则加入分页，并对查询参数进行检查、补齐。
  2、对count语句进行分析，如果不存在，则自动生成，并对参数进行检查、删除或补齐。
* 可以自提供count语句，count语句与查询语句定义在同一块中。（推荐做法）
* 执行器构建一个新的MappedStatement执行count语句。
* 该执行器比较灵活，但受到不同数据库语言差异的影响，效率次于SingleQueryStaticPageExecutor。

SingleQueryStaticPageExecutor

* 单语句静态分页执行器。
* 适用于JDBC只支持单语句执行的情况。
* 由用户自己定义分页查询语句与count语句，分页语句与count语句定义在不同的块中。
* count语句通过PageInfoWithCountMappedStatement#countMappedStatementId指定。
* 构建一个新的MappedStatement执行count语句。
* 该执行器不受数据库语言差异的影响，分页查询语句及count语句完全由用户提供，执行效率较高。

## Page：页面接口

具体实现类：

PageInfo：适用于MultiQueriesDynamicPageExecutor、MultiQueriesStaticPageExecutor（查询与统计定义在同一查询块中）、SingleQueryDynamicPageExecutor（查询与统计定义在同一查询块中）。

PageInfoWithCountMappedStatement：适用于SingleQueryStaticPageExecutor（查询与统计定义在不同查询块中）。

## PageSelectStatementProvider

代理SelectStatementProvider,加入PageInfo参数。

# 使用方法

## 加载依赖

```
        <dependency>
            <groupId>io.github.software-hhao</groupId>
            <artifactId>hhao-mybatis</artifactId>
        </dependency>
```

## 一个分页查询的示例

1. BookMapper.xml

```
   <select id="findBook" resultMap="BookResult">
        select book1.id,name,price,publicDate,recordDateTime from book as book1 
        inner outer join (
            select id from book 
            ${whereClauseProvider.whereClause} 
            order by id 
            limit #{pageInfo.limit,jdbcType=BIGINT}
            offset #{pageInfo.offset,jdbcType=BIGINT}
        ) as book2
        on book1.id = book2.id
        ${pageInfo.orderBySql};
        select count(*) as count from book ${whereClauseProvider.whereClause}
    </select>
```

2. BookMapper接口

```
    @Mapper
    List<Book> findBook(@Param("whereClauseProvider") WhereClauseProvider where, @Param("pageInfo") PageInfo pageInfo);
```

3. BookServer

```
public PageResponse<Book> findBook(final BookPageQuery bookPageQuery) {
        // 构建 PageInfo
        PageInfo pageInfo = new PageInfo.Builder(bookPageQuery)
                .SingleQueryDynamicPageExecutor()
                .addOrderTable(new PageInfo.OrderTable(BookDynamicSqlSupport.book.tableNameAtRuntime(),  BookMapper.selectList))
                .build();
        // 构建where
        WhereClauseProvider provider =null;
        try {
            provider = SqlBuilder
                    .where(BookDynamicSqlSupport.recordDateTime, isBetweenWhenPresent(bookPageQuery.getStart()).and(bookPageQuery.getEnd())
	            .filter((localDateTime1, localDateTime2) -> {
                        // 检查recordDateTime是否不为null，以及是否需要包含该条件
                        return localDateTime1 != null && localDateTime2 != null && bookPageQuery.getParams().contains(BookDynamicSqlSupport.recordDateTime.name());
                    }))
                    .and(BookDynamicSqlSupport.name, isLike(bookPageQuery.getName()).map(s -> {
                        return s + "%";
                    })
                    .filter(s -> {
                        return s != null && !s.isBlank() && bookPageQuery.getParams().contains(BookDynamicSqlSupport.name.name());
                    }))
                    .build()
                    .render(RenderingStrategies.MYBATIS3, "whereClauseProvider").get();
        }catch(NonRenderingWhereClauseException e){
            //如果where条件为空，则返回null，默认就返回所有数据，也可以直接返回空数据集
            provider=null;
        }
        bookMapper.findBook(provider, pageInfo);
        return pageInfo.of();
    }
```

4. BookPageQuery

```
public class BookPageQuery extends DynamicParamsPageQuery {
    // 查询条件
    private LocalDateTime start;
    private LocalDateTime end;
    private String name;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
```
