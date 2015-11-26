/* global define:false */
define([], function () {
  'use strict';

  function yForColor(color) {
    switch(color) {
      case 'r':
        return 0;
      case 'g':
        return 1;
      case 'b':
        return 2;
      case 'y':
        return 3;
      case 'w':
        return 4;
      default:
        throw 'Invalid color [' + color + '].';
    }
  }

  function xForRole(gem) {
    if(gem.crash) {
      return 15;
    } else if(gem.timer !== undefined) {
      return parseInt(gem.timer);
    } else if(gem.width > 0 || gem.height > 0) {
      var role = gem.role;
      switch(role) {
        case 'ul':
          return 6;
        case 't':
          return 7;
        case 'ur':
          return 8;
        case 'l':
          return 9;
        case 'c':
          return 10;
        case 'r':
          return 11;
        case 'bl':
          return 12;
        case 'b':
          return 13;
        case 'br':
          return 14;
        default:
          throw role;
      }
    } else {
      return 0;
    }
  }

  return {
    spriteFor: function(gem) {
      var x = xForRole(gem);
      var y = yForColor(gem.color);
      return (y * 16) + x;
    }
  };
});
