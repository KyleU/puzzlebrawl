/* global define:false */
/* global Phaser:false */
define(['gem/Gem', 'utils/Config'], function (Gem, Config) {
  'use strict';

  function addGem(board, gem, x, y) {
    var g = new Gem(gem, board.game);
    g.x = x * Config.tile.size;
    g.y = (board.h - y) * Config.tile.size;
    board.gems[gem.id] = g;
    board.add(g);
  }

  function changeGem(board, originalGem, newGem) {
    var gem = board.gems[originalGem.id];
    gem.updateModel(newGem);
  }

  function moveGem(board, originalGem, x, y, xDelta, yDelta) {
    var gem = board.gems[originalGem.id];
    if(gem === null || gem === undefined) {
      throw 'Gem with id [' + originalGem.id + '] is not present.';
    }
    var targetX = (x + xDelta) * Config.tile.size;
    var targetY = (board.h - (y + yDelta)) * Config.tile.size;

    var tween = board.game.add.tween(gem);
    tween.to({ x: targetX, y: targetY }, Config.animation.removeDurationMs, Phaser.Easing.Cubic.Out);
    tween.start();
  }

  function moveGems(board, moves) {
    for(var moveIdx = 0; moveIdx < moves.length; moveIdx++) {
      var move = moves[moveIdx];
      var gem = board.at(move.x, move.y);
      moveGem(board, gem, move.x, move.y, move.xDelta, move.yDelta);
    }
  }

  function removeGem(board, g) {
    var gem = board.gems[g.id];
    if(gem === null || gem === undefined) {
      throw 'Gem with id [' + g.id + '] has not been added.';
    }

    var tween = board.game.add.tween(gem);
    tween.to({ alpha: 0 }, Config.animation.removeDurationMs, Phaser.Easing.Cubic.Out);
    tween.onComplete.add(function() {
      board.remove(gem);
    });
    tween.start();
  }

  return {
    addGem: addGem,
    changeGem: changeGem,
    moveGem: moveGem,
    moveGems: moveGems,
    removeGem: removeGem
  };
});
