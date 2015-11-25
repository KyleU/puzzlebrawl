/* global define:false */
define([], function () {
  'use strict';

  function GemSprites(client) {
    client.load.spritesheet('gems', 'assets/images/game/gems.png', 128, 128);
  }

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
      case '5':
        return 0;
      case '4':
        return 1;
      case '3':
        return 2;
      case '2':
        return 3;
      case '1':
        return 4;
      case 'ul':
        return 5;
      case 'u':
        return 6;
      case 'ur':
        return 7;
      case 'l':
        return 8;
      case 'c':
        return 9;
      case 'r':
        return 10;
      case 'bl':
        return 11;
      case 'b':
        return 12;
      case 'br':
        return 13;
      case undefined:
        return 14;
      case '':
        return 14;
      case null:
        return 14;
      case 'x':
        return 15;
      default:
        throw 'Invalid role [' + role + '].';
    }
  }

  GemSprites.prototype.spriteFor = function(gem) {
    var x = xForRole(gem.role);
    var y = yForColor(gem.color);
    return { x: x, y: y };
  };

  return GemSprites;
});
