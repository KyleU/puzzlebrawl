/* global define:false */
define([], function () {
  function log(e, indent) {
    var indentLevel;
    if(indent === undefined) {
      indentLevel = 0;
    } else {
      indentLevel = indent;
    }
    var padding = '';
    for(var indentIdx = 0; indentIdx < indentLevel; indentIdx++) {
      padding += '  ';
    }
    if(e.action === undefined) {
      console.log(padding + e.title + ':');
      for(var childIdx = 0; childIdx < e.children.length; childIdx++) {
        var child = e.children[childIdx];
        log(child, indentLevel + 1);
      }
    } else {
      console.log(padding + e.title + ': ' + e.action);
    }
  }

  function Menu(entry) {
    this.root = entry;
    log(this.root);
  }

  Menu.prototype.show = function() {
    console.log('Show menu!');
  };

  return Menu;
});
