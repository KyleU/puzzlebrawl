package models.user

final case class UserPreferences(
  avatar: String = "guest",
  theme: String = "dark")
