package com.novoda.sqliteprovider.demo.domain;

import com.novoda.sqliteprovider.demo.persistance.DatabaseConstants.RawSql;
import com.novoda.sqliteprovider.demo.provider.FireworkUriConstants;
import com.novoda.sqliteprovider.demo.util.Log;

public final class UseCaseFactory {

    private UseCaseFactory() {
    }

    public enum UseCase {
        ADD, BULK_ADD, BULK_YIELD_ADD, BULK_WIHOUT_YIELD, ONE_TO_MANY, PRIMARY_KEY_LOOKUP, SELECT_ALL, DISTINCT, LIMIT, GROUP_HAVING;
    }

    public static UseCaseInfo getInfo(UseCase useCase) {
        switch (useCase) {
            case ADD:
                return createUseCaseInfo(FireworkUriConstants.ADD_FIREWORK, RawSql.INSERT_FIREWORK);
            case BULK_ADD:
                return createUseCaseInfo(FireworkUriConstants.ADD_FIREWORK, RawSql.BULK_INSERT_FIREWORKS);
            case BULK_YIELD_ADD:
                return createUseCaseInfo(FireworkUriConstants.ADD_FIREWORK_YIELD, RawSql.BULK_INSERT_FIREWORKS);
            case BULK_WIHOUT_YIELD:
                return createUseCaseInfo(FireworkUriConstants.ADD_FIREWORK_WITHOUT_YIELD, RawSql.BULK_INSERT_FIREWORKS);
            case ONE_TO_MANY:
                return createUseCaseInfo(FireworkUriConstants.ONE_TO_MANY_SEARCH, RawSql.SELECT_USING_SHOP_FOREIGN_KEY);
            case PRIMARY_KEY_LOOKUP:
                return createUseCaseInfo(FireworkUriConstants.PRIMARY_KEY_SEARCH, RawSql.SELECT_USING_PRIMARY_KEY);
            case SELECT_ALL:
                return createUseCaseInfo(FireworkUriConstants.VIEW_ALL_SEARCH, RawSql.SELECT_ALL);
            case DISTINCT:
                return createUseCaseInfo(FireworkUriConstants.UNIQUE_SEARCH, RawSql.DISTINCT);
            case LIMIT:
                return createUseCaseInfo(FireworkUriConstants.LIMIT_3, RawSql.LIMIT);
            case GROUP_HAVING:
                return createUseCaseInfo(FireworkUriConstants.GROUP_BY_SEARCH, RawSql.GROUP_BY);
            default:
                Log.e("UseCase " + useCase.toString() + " not found, returning null safe case.");
                return UseCaseInfo.getNullSafe();
        }
    }

    private static UseCaseInfo createUseCaseInfo(String uri, String sql) {
        return new UseCaseInfo(uri, sql);
    }

}
