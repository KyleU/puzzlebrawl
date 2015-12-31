package models.ddl

case object CreateBrawlsTable extends CreateTableStatement("brawls") {
  override val sql = s"""
    create table $tableName (
      id uuid not null primary key,
      seed int not null,
      scenario character varying(128) not null,
      status character varying(128) not null,
      players uuid[] not null,
      normal_gems integer[] not null,
      timer_gems integer[] not null,
      moves integer[] not null,
      created timestamp not null default now(),
      first_move timestamp,
      completed timestamp,
      logged timestamp
    ) with (oids=false);
  """
}
