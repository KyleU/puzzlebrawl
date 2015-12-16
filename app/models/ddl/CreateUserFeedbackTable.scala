package models.ddl

object CreateUserFeedbackTable extends CreateTableStatement("user_feedback") {
  override def sql: String = s"""
    create table $tableName (
      id uuid not null primary key,
      user_id uuid not null,
      feedback text not null,
      occurred timestamp without time zone not null
    ) with (oids=false);

    alter table $tableName add constraint user_feedback_users_fk foreign key (user_id) references users (id) on update no action on delete no action;
    create index user_feedback_users_idx on $tableName using btree (user_id);
  """
}
