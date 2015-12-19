/* global define:false */
define([], function () {
  'use strict';

  return {
    'check': function(src, tgt) {
      var errors = [];

      function e(key, s, t) {
        if(s !== t) {
          errors.push(key + ': ' + s + ' !== ' + t);
        }
      }

      console.log(src);
      console.log(tgt);

      e('id', src.id, tgt.id);
      e('player-count', src.players.length, tgt.players.length);
      for(var playerIdx = 0; playerIdx < src.players.length; playerIdx++) {
        var srcPlayer = src.players[playerIdx];
        var tgtPlayer = tgt.players[playerIdx];
        e('player-' + playerIdx + '-id', srcPlayer.id, tgtPlayer.id);
        e('player-' + playerIdx + '-name', srcPlayer.name, tgtPlayer.name);
        e('player-' + playerIdx + '-gemstream-seed', srcPlayer.gemStream.seed, tgtPlayer.gemStream.seed);
        e('player-' + playerIdx + '-score', srcPlayer.score, tgtPlayer.score);

        var srcBoard = srcPlayer.board;
        var tgtBoard = tgtPlayer.board;

        e('player-' + playerIdx + '-board-width', srcBoard.width, tgtBoard.width);
        e('player-' + playerIdx + '-board-height', srcBoard.height, tgtBoard.height);

        for(var y = 0; y < srcBoard.height; y++) {
          for(var x = 0; x < srcBoard.width; x++) {
            var srcG = srcBoard.spaces[x][y];
            var tgtG = tgtBoard.spaces[x][y];
            if(srcG === null || tgtG === null) {
              e('player-' + playerIdx + '-board-' + x + '-' + y, srcG === null, tgtG === null);
            } else {
              e('player-' + playerIdx + '-board-' + x + '-' + y, srcG.id, tgtG.id);
            }
          }
        }
      }

      console.log('Sync completed with [' + errors.length + '] errors.');
      for(var errorIdx = 0; errorIdx < errors.length; errorIdx++) {
        console.log('  - ' + errors[errorIdx]);
      }
    }
  };
});
