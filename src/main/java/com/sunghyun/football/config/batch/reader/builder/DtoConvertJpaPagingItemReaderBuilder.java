package com.sunghyun.football.config.batch.reader.builder;

import com.sunghyun.football.config.batch.reader.DtoConvertJpaPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.util.Assert;

import java.util.Map;

public class DtoConvertJpaPagingItemReaderBuilder<D,T> {

    private int pageSize = 10;

    private EntityManagerFactory entityManagerFactory;

    private Map<String, Object> parameterValues;

    private boolean transacted = true;

    private String queryString;

    private JpaQueryProvider queryProvider;

    private boolean saveState = true;

    private String name;

    private int maxItemCount = Integer.MAX_VALUE;

    private int currentItemCount;

    private Class<T> dtoClass;


    /**
     * Configure if the state of the
     * {@link org.springframework.batch.item.ItemStreamSupport} should be persisted within
     * the {@link org.springframework.batch.item.ExecutionContext} for restart purposes.
     * @param saveState defaults to true
     * @return The current instance of the builder.
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> saveState(boolean saveState) {
        this.saveState = saveState;

        return this;
    }

    /**
     * The name used to calculate the key within the
     * {@link org.springframework.batch.item.ExecutionContext}. Required if
     * {@link #saveState(boolean)} is set to true.
     * @param name name of the reader instance
     * @return The current instance of the builder.
     * @see org.springframework.batch.item.ItemStreamSupport#setName(String)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> name(String name) {
        this.name = name;

        return this;
    }

    /**
     * Configure the max number of items to be read.
     * @param maxItemCount the max items to be read
     * @return The current instance of the builder.
     * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#setMaxItemCount(int)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> maxItemCount(int maxItemCount) {
        this.maxItemCount = maxItemCount;

        return this;
    }

    /**
     * Index for the current item. Used on restarts to indicate where to start from.
     * @param currentItemCount current index
     * @return this instance for method chaining
     * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#setCurrentItemCount(int)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> currentItemCount(int currentItemCount) {
        this.currentItemCount = currentItemCount;

        return this;
    }

    /**
     * The number of records to request per page/query. Defaults to 10. Must be greater
     * than zero.
     * @param pageSize number of items
     * @return this instance for method chaining
     * @see JpaPagingItemReader#setPageSize(int)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> pageSize(int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * A map of parameter values to be set on the query. The key of the map is the name of
     * the parameter to be set with the value being the value to be set.
     * @param parameterValues map of values
     * @return this instance for method chaining
     * @see JpaPagingItemReader#setParameterValues(Map)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> parameterValues(Map<String, Object> parameterValues) {
        this.parameterValues = parameterValues;

        return this;
    }

    /**
     * A query provider. This should be set only if {@link #queryString(String)} have not
     * been set.
     * @param queryProvider the query provider
     * @return this instance for method chaining
     * @see JpaPagingItemReader#setQueryProvider(JpaQueryProvider)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> queryProvider(JpaQueryProvider queryProvider) {
        this.queryProvider = queryProvider;

        return this;
    }

    /**
     * The HQL query string to execute. This should only be set if
     * {@link #queryProvider(JpaQueryProvider)} has not been set.
     * @param queryString the HQL query
     * @return this instance for method chaining
     * @see JpaPagingItemReader#setQueryString(String)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> queryString(String queryString) {
        this.queryString = queryString;

        return this;
    }

    /**
     * Indicates if a transaction should be created around the read (true by default). Can
     * be set to false in cases where JPA implementation doesn't support a particular
     * transaction, however this may cause object inconsistency in the
     * EntityManagerFactory.
     * @param transacted defaults to true
     * @return this instance for method chaining
     * @see JpaPagingItemReader#setTransacted(boolean)
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> transacted(boolean transacted) {
        this.transacted = transacted;

        return this;
    }

    /**
     * The {@link EntityManagerFactory} to be used for executing the configured
     * {@link #queryString}.
     * @param entityManagerFactory {@link EntityManagerFactory} used to create
     * {@link jakarta.persistence.EntityManager}
     * @return this instance for method chaining
     */
    public DtoConvertJpaPagingItemReaderBuilder<D,T> entityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

        return this;
    }

    public DtoConvertJpaPagingItemReaderBuilder<D,T> dtoClass(Class<T> dtoClass) {
        this.dtoClass = dtoClass;

        return this;
    }

    /**
     * Returns a fully constructed {@link JpaPagingItemReader}.
     * @return a new {@link JpaPagingItemReader}
     */
    public DtoConvertJpaPagingItemReader<D,T> build() {
        Assert.isTrue(this.pageSize > 0, "pageSize must be greater than zero");
        Assert.notNull(this.entityManagerFactory, "An EntityManagerFactory is required");

        if (this.saveState) {
            Assert.hasText(this.name, "A name is required when saveState is set to true");
        }

        if (this.queryProvider == null) {
            Assert.hasLength(this.queryString, "Query string is required when queryProvider is null");
        }

        DtoConvertJpaPagingItemReader<D,T> reader = new DtoConvertJpaPagingItemReader<>();

        reader.setQueryString(this.queryString);
        reader.setPageSize(this.pageSize);
        reader.setParameterValues(this.parameterValues);
        reader.setEntityManagerFactory(this.entityManagerFactory);
        reader.setQueryProvider(this.queryProvider);
        reader.setTransacted(this.transacted);
        reader.setCurrentItemCount(this.currentItemCount);
        reader.setMaxItemCount(this.maxItemCount);
        reader.setSaveState(this.saveState);
        reader.setName(this.name);
        reader.setDtoClass(this.dtoClass);

        return reader;
    }

}
