package models.ddl

object CreateUserFeedbackNotesTable extends CreateTableStatement("user_feedback_notes") {
  override def sql: String = s"""
    create table $tableName (
      id uuid not null primary key,
      feedback_id uuid not null,
      author_id uuid not null,
      content text not null,
      occurred timestamp without time zone not null
    ) with (oids=false);

    alter table $tableName add constraint user_feedback_note_user_feedback_fk
      foreign key (feedback_id) references user_feedback (id) on update no action on delete no action;

    alter table $tableName add constraint user_feedback_note_users_fk
      foreign key (author_id) references users (id) on update no action on delete no action;

    create index user_feedback_notes_feedback_id_idx on $tableName using btree (feedback_id);
  """
}
