/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['gem/Gem'], function (Gem) {
  'use strict';

  return {
    addGem: function(board, gem, x, y) {
      var original = board.gemLocations[gem.id];
      if(original !== null && original !== undefined) {
        throw 'Gem [' + gem.id + '] has already been added.';
      }
      var activeGem = _.find(board.activeGemLocations, function(ag) {
        return ag[0].model.id === gem.id;
      });

      if(activeGem === undefined) {
        var g = new Gem(gem, board.game);
        g.x = x * 128;
        g.y = board.height - (y * 128);
        board.gemLocations[gem.id] = [g, x, y];
        board.add(g);
      } else {
        board.activeGemLocations = _.reject(board.activeGemLocations, function(g) {
          return g[0].model.id === gem.id;
        });
        board.gemLocations[gem.id] = activeGem;
        board.moveGem(activeGem[1], activeGem[2], x - activeGem[1], y - activeGem[2]);
      }
    },

    changeGem: function(board, newGem, x, y) {
      var g = board.at(x, y);
      g[0].updateModel(newGem);
    },

    moveGem: function(board, x, y, xDelta, yDelta) {
      var g = board.at(x, y);
      if(g === null || g === undefined) {
        throw 'Gem at [' + x + ', ' + y + '] is not present.';
      }
      board.gemLocations[g[0].model.id] = [g[0], x + xDelta, y + yDelta];
      var tween = board.game.add.tween(g[0]);
      tween.to({x: (x + xDelta) * 128, y: board.height - ((y + yDelta) * 128)}, 200, Phaser.Easing.Cubic.Out);
      tween.start();
    },

    removeGem: function(board, x, y) {
      var g = board.at(x, y);
      if(g === null || g === undefined) {
        throw 'Gem at [' + x + ', ' + y + '] has not been added.';
      }
      delete board.gemLocations[g[0].model.id];
      board.remove(g[0]);
    }
  };
});
