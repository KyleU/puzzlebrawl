/* global define:false */
/* global Phaser:false */
define(['gem/Gem'], function (Gem) {
  'use strict';

  return {
    addGem: function(board, gem, x, y) {
      var original = board.gems[gem.id];
      if(original !== null && original !== undefined) {
        throw 'Gem [' + gem.id + '] has already been added.';
      }
      board.set(x, y, gem);
      var g = new Gem(gem, board.game);
      g.x = x * 128;
      g.y = board.height - (y * 128);
      board.gems[gem.id] = g;
      board.add(g);
    },

    changeGem: function(board, newGem, x, y) {
      var g = board.at(x, y);
      if(g === null) {
        throw 'Cannot change empty space.';
      }
      if(g.id !== newGem.id) {
        throw 'Gem at [' + x + ', ' + y + '] has id [' + g.id + '], not [' + newGem.id + '].';
      }
      var gem = board.gems[g.id];
      board.set(x, y, newGem);
      gem.updateModel(newGem);
    },

    moveGem: function(board, x, y, xDelta, yDelta) {
      var g = board.at(x, y);
      if(g === null || g === undefined) {
        throw 'Gem at [' + x + ', ' + y + '] is not present.';
      }

      var gem = board.gems[g.id];
      if(g === null || g === undefined) {
        throw 'Gem with id [' + g.id + '] is not present.';
      }

      board.clear(x, y, g.width, g.height);
      board.set(x + xDelta, y + yDelta, g);
      var tween = board.game.add.tween(gem);
      tween.to({x: (x + xDelta) * 128, y: board.height - ((y + yDelta) * 128)}, 200, Phaser.Easing.Cubic.Out);
      tween.start();
    },

    removeGem: function(board, x, y) {
      var g = board.at(x, y);
      if(g === null || g === undefined) {
        throw 'Gem at [' + x + ', ' + y + '] has not been added.';
      }
      var gem = board.gems[g.id];
      if(gem === null || gem === undefined) {
        throw 'Gem with id [' + g.id + '] has not been added.';
      }

      delete board.gems[g.id];
      board.clear(x, y, g.width, g.height);
      board.remove(gem);
    }
  };
});
