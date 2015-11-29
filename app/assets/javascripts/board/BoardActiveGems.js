/* global define:false */
/* global Phaser:false */
define(['gem/Gem'], function (Gem) {
  'use strict';

  function addActiveGem(board, gem, x, y) {
    var g = new Gem(gem, board.game);
    g.x = x * 128;
    g.y = board.height - (y * 128);
    board.activeGemLocations.push([g, x, y]);
    board.add(g);
  }

  function moveActiveGems(board, ags) {
    for(var agIdx = 0; agIdx < ags.length; agIdx++) {
      var g = board.activeGemLocations[agIdx];
      var newLoc = ags[agIdx];
      board.activeGemLocations[agIdx] = [g[0], newLoc.x, newLoc.y];
      var tween = board.game.add.tween(g[0]);
      tween.to({x: newLoc.x * 128, y: board.height - (newLoc.y * 128)}, 200, Phaser.Easing.Cubic.Out);
      tween.start();
    }
  }

  return {
    setActiveGems: function(board, ags) {
      if(board.activeGemLocations.length === 0) {
        for(var newIdx = 0; newIdx < ags.length; newIdx++) {
          var newAg = ags[newIdx];
          addActiveGem(board, newAg.gem, newAg.x, newAg.y);
        }
      } else {
        for(var agIdx = 0; agIdx < ags.length; agIdx++) {
          var original = board.activeGemLocations[agIdx];
          var ag = ags[agIdx];
          if(original[0].id !== ag.id) {
            throw 'Incorrect active gem at index [' + agIdx + '].';
          }
        }
        moveActiveGems(board, ags);
      }
    }
  };
});
