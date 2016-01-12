package models.ui

case class MenuEntry(title: String, action: Option[String] = None, children: Option[Seq[MenuEntry]] = None)
