/* global define:false */
/* global Phaser:false */
define(['board/BoardAnimations', 'utils/Config'], function (BoardAnimations, Config) {
  'use strict';

  return {
    addGem: function(board, gem, x, y) {
      var original = board.gems[gem.id];
      if(original !== null && original !== undefined) {
        throw new Error('Gem [' + gem.id + '] has already been added.');
      }
      board.set(x, y, gem);

      BoardAnimations.addGem(board, gem, x, y);
    },

    changeGem: function(board, newGem, x, y) {
      var g = board.at(x, y);
      if(g === null) {
        throw new Error('Cannot change empty space at [' + x + ', ' + y + '].');
      }
      if(g.id !== newGem.id) {
        throw new Error('Gem at [' + x + ', ' + y + '] has id [' + g.id + '], not [' + newGem.id + '].');
      }
      board.set(x, y, newGem);

      BoardAnimations.changeGem(board, g, newGem);
    },

    moveGem: function(board, x, y, xDelta, yDelta) {
      var g = board.at(x, y);
      if(g === null || g === undefined) {
        throw new Error('Gem at [' + x + ', ' + y + '] is not present.');
      }

      BoardAnimations.moveGem(board, g, x, y, xDelta, yDelta);

      board.clear(x, y, g.width, g.height);
      board.set(x + xDelta, y + yDelta, g);
    },

    moveGems: function(board, moves) {
      BoardAnimations.moveGems(board, moves);

      var gems = [];
      for(var gemMoveIdx = 0; gemMoveIdx < moves.length; gemMoveIdx++) {
        var gemMove = moves[gemMoveIdx];
        var gem = board.at(gemMove.x, gemMove.y);
        if(gem === null) {
          throw new Error('Move attempted from empty position [' + gemMove.x + ', ' + gemMove.y + '].');
        }
        board.clear(gemMove.x, gemMove.y, gem.width, gem.height);
        gems.push(gem);
      }

      for(var moveIdx = 0; moveIdx < moves.length; moveIdx++) {
        var move = moves[moveIdx];
        var moveGem = gems[moveIdx];
        board.set(move.x + move.xDelta, move.y + move.yDelta, moveGem);

        var targetX = (move.x + move.xDelta) * Config.tile.size;
        var targetY = (board.h - (move.y + move.yDelta)) * Config.tile.size;

        var tween = board.game.add.tween(board.gems[moveGem.id]);
        tween.to({x: targetX, y: targetY}, 200, Phaser.Easing.Cubic.Out);
        tween.start();
      }
    },

    removeGem: function(board, x, y) {
      var g = board.at(x, y);
      if(g === null || g === undefined) {
        throw new Error('Gem at [' + x + ', ' + y + '] has not been added.');
      }

      BoardAnimations.removeGem(board, g);

      delete board.gems[g.id];
      board.clear(x, y, g.width, g.height);
    }
  };
});
