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

  function xForRole(role) {
    switch(role) {
      case undefined:
        return 0;
      case '':
        return 0;
      case null:
        return 0;
      case '1':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case 'ul':
        return 6;
      case 'u':
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
      case 'x':
        return 15;
      default:
        throw 'Invalid role [' + role + '].';
    }
  }

  return {
    spriteFor: function(gem) {
      var x = xForRole(gem.role);
      var y = yForColor(gem.color);
      return (y * 16) + x;
    }
  };
});
