package models.ddl

case object CreateClientTraceTable extends CreateTableStatement("client_trace") {
  override val sql = s"""
    create table $tableName (
      id uuid not null primary key,
      player uuid not null,
      data json not null,
      created timestamp not null
    ) with (oids=false);

    alter table $tableName add constraint client_trace_users_fk foreign key (player) references users (id) on update no action on delete no action;
  """
}
