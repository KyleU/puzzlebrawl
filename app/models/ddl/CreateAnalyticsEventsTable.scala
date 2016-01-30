package models.ddl

case object CreateAnalyticsEventsTable extends CreateTableStatement("analytics_events") {
  override val sql = s"""
    create table $tableName (
      id uuid not null primary key,
      event_type character varying(64) not null,
      user_id uuid not null,
      source_address character varying(128),
      data json not null,
      created timestamp not null
    ) with (oids=false);

    create index ${tableName}_user_id_idx on $tableName using btree (user_id);
  """
}

