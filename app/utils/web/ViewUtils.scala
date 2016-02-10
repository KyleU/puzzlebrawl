package utils.web

import play.twirl.api.Html

object ViewUtils {
  def th(key: String, label: String, selected: String, link: Boolean = true) = {
    val ret = if (!link) {
      label
    } else if (selected == key) {
      s"""$label <span class="caret"></span>"""
    } else {
      s"""<a href="?sortBy=$key">$label</a></th>"""
    }
    Html(s"""<th nowrap="nowrap" class="th-$key">$ret</th>""")
  }
}
