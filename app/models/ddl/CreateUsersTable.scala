package models.ddl

case object CreateUsersTable extends CreateTableStatement("users") {
  override val sql = s"""
    create table $tableName (
      id uuid primary key,
      username character varying(256),
      prefs json NOT NULL,
      profiles text[] not null,
      roles character varying(64)[] not null,
      created timestamp not null
    ) with (oids=false);

    create index users_profiles_idx on $tableName using gin (profiles);
    create unique index users_username_idx on $tableName using btree (username collate pg_catalog."default");
    create index users_roles_idx on $tableName using gin (roles);
  """
}
