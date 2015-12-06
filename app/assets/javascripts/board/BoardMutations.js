/* global define:false */
/* global _:false */
define([], function () {
  'use strict';

  var debug = true;

  function applyMutation(board, m) {
    switch(m.t) {
      case 'a':
        if(debug) { console.log('    Add: ' + JSON.stringify(m.v)); }
        board.addGem(m.v.gem, m.v.x, m.v.y);
        break;
      case 'm':
        if(debug) { console.log('    Move: ' + JSON.stringify(m.v)); }
        board.moveGem(m.v.x, m.v.y, m.v.xDelta, m.v.yDelta);
        break;
      case 'x':
        if(debug) { console.log('    Moves: ' + JSON.stringify(m.v)); }
        board.moveGems(m.v.moves);
        break;
      case 'c':
        if(debug) { console.log('    Change: ' + JSON.stringify(m.v)); }
        board.changeGem(m.v.newGem, m.v.x, m.v.y);
        break;
      case 'r':
        if(debug) { console.log('    Remove: ' + JSON.stringify(m.v)); }
        board.removeGem(m.v.x, m.v.y);
        break;
      default:
        console.log('Unhandled mutation [' + m.t + '].');
        break;
    }
  }

  function applySegment(board, segment, idx) {
    if(debug) { console.log('  Segment [' + idx + ': ' + segment.category + '] (' + segment.mutations.length + ' mutations):'); }
    _.each(segment.mutations, function(mutation) {
      applyMutation(board, mutation);
    });
  }

  return {
    applyMutations: function(board, segments) {
      if(debug) {
        var count = _.reduce(segments, function(i, c){ return i + c.length; }, 0);
        console.log('Processing [' + segments.length + '] segments containing [' + count + '] mutations.');
      }
      _.each(segments, function(segment, idx) {
        applySegment(board, segment, idx);
      });
    }
  };
});
