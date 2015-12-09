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

  function xForRole(gem, x, y, width, height) {
    if(gem.crash) {
      return 15;
    } else if(gem.timer !== undefined) {
      return parseInt(gem.timer);
    } else {
      if(gem.width > 1 || gem.height > 1) {
        if(y === 0) {
          if(x === 0) {
            return 12; // bl
          } else if(x === gem.width - 1) {
            return 14; // br
          } else {
            return 13; // b
          }
        } else if(y === gem.height - 1) {
          if(x === 0) {
            return 6; // ul
          } else if(x === gem.width - 1) {
            return 8; // ur
          } else {
            return 7; // t
          }
        } else {
          if(x === 0) {
            return 9; // l
          } else if(x === gem.width - 1) {
            return 11; // r
          } else {
            return 10; // t
          }
        }
      } else {
        return 0;
      }
    }
  }

  return {
    spriteFor: function(gem, x, y) {
      var col = xForRole(gem, x, y);
      var row = yForColor(gem.color);
      return (row * 16) + col;
    }
  };
});
