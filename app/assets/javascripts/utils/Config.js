/* global define:false */
define(function () {
  'use strict';

  return {
    tile: {
      size: 128
    },

    input: {
      tapMaxDistancePx: 10,
      holdTimeMs: 250,
      swipeMinTimeMs: 50,
      swipeMaxTimeMs: 250,
      swipeDistancePx: 100
    },

    animation: {
      moveDurationMs: 200,
      removeDurationMs: 200
    }
  };
});
