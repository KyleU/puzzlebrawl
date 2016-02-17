/* global define:false */
/* global _:false */
define(['utils/Config'], function (Config) {
  'use strict';

  function targetFor(playmat, pointer) {
    var srcX = pointer.positionDown.x;
    var srcY = pointer.positionDown.y;

    return _.find(playmat.players, function(player) {
      var minX = (player.group.x * playmat.scale.x) + playmat.x;
      var maxX = ((player.group.x + player.group.board.width) * playmat.scale.x) + playmat.x;
      var minY = (player.group.y * playmat.scale.y) + playmat.y;
      var maxY = ((player.group.y + player.group.board.height) * playmat.scale.y) + playmat.y;
      return srcX >= minX && srcX <= maxX && srcY >= minY && srcY <= maxY;
    });
  }

  var PlaymatInput = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatInput.prototype.onTap = function(pointer) {
    var t = targetFor(this.playmat, pointer);
    if(t !== undefined) {
      if(t.id === this.playmat.self) {
        this.playmat.game.onInput('active-clockwise');
      } else {
        this.playmat.game.onInput('target-select', t.id);
      }
    }
  };

  PlaymatInput.prototype.onHold = function(pointer) {
    var t = targetFor(this.playmat, pointer);
    if(t !== undefined) {
      if(t.id === this.playmat.self) {
        this.playmat.game.onInput('active-counter-clockwise');
      }
    }
  };

  PlaymatInput.prototype.onSwipe = function(pointer) {
    var t = targetFor(this.playmat, pointer);
    if(t !== undefined) {
      var xDiff = pointer.position.x - pointer.positionDown.x;
      var yDiff = pointer.position.y - pointer.positionDown.y;

      if(Math.abs(xDiff) > (Math.abs(yDiff) * 2)) {
        if(xDiff > 0) {
          if(t.id === this.playmat.self) {
            this.playmat.game.onInput('active-right');
          } else {
            this.playmat.game.onInput('target-next');
          }
        } else {
          if(t.id === this.playmat.self) {
            this.playmat.game.onInput('active-left');
          } else {
            this.playmat.game.onInput('target-previous');
          }
        }
      } else if(Math.abs(yDiff) > (Math.abs(xDiff) * 2)) {
        if(yDiff > 0) {
          if(t.id === this.playmat.self) {
            this.playmat.game.onInput('active-drop');
          }
        } else {
          if(t.id === this.playmat.self) {
            this.playmat.game.onInput('active-step');
          }
        }
      }
    }
  };

  return PlaymatInput;
});
