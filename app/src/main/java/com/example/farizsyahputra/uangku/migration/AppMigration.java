package com.example.farizsyahputra.uangku.migration;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class AppMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            final RealmObjectSchema moneyTrafficSchema = schema.get("MoneyTraffic");

            moneyTrafficSchema.addField("Description", String.class);
            moneyTrafficSchema.removeField("UpdatedAt");
        }

        if (newVersion == 2) {
            final RealmObjectSchema moneyTrafficSchema = schema.get("MoneyTraffic");

            moneyTrafficSchema.removeField("CreatedAt");
            moneyTrafficSchema.addField("CreatedAt", Long.class);
        }
    }
}
