package models.ddl

case object CreateAdHocQueriesTable extends CreateTableStatement("adhoc_queries") {
  override val sql = s"""
    create table $tableName (
      id uuid not null primary key,
      title text not null,
      author uuid not null,
      sql text not null,
      params text[],
      created timestamp not null,
      updated timestamp not null
    ) with (oids=false);

    alter table $tableName add constraint adhoc_queries_users_fk foreign key (author) references users (id) on update no action on delete no action;
  """
}
