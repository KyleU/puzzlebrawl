/* global define:false */
define([], function () {
  'use strict';

  function setScore(board, score) {
    console.log('Setting score for [' + board.key + '] to [' + score + '].');
    board.score = score;
  }

  return {
    init: function(board, score) {
      // TODO init sprites
      setScore(board, score);
    },

    changeScore: function(board, delta) {
      var current = board.score;
      var newScore = current + delta;
      setScore(board, newScore);
    }
  };
});
