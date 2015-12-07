/* global define:false */
/* global _:false */
define(['board/BoardGems'], function (BoardGems) {
  'use strict';

  var debug = false;

  function applyMutation(board, m) {
    switch(m.t) {
      case 'a':
        if(debug) { console.log('    Add: ' + JSON.stringify(m.v)); }
        BoardGems.addGem(board, m.v.gem, m.v.x, m.v.y);
        break;
      case 'm':
        if(debug) { console.log('    Move: ' + JSON.stringify(m.v)); }
        BoardGems.moveGem(board, m.v.x, m.v.y, m.v.xDelta, m.v.yDelta);
        break;
      case 'x':
        if(debug) { console.log('    Moves: ' + JSON.stringify(m.v)); }
        BoardGems.moveGems(board, m.v.moves);
        break;
      case 'c':
        if(debug) { console.log('    Change: ' + JSON.stringify(m.v)); }
        BoardGems.changeGem(board, m.v.newGem, m.v.x, m.v.y);
        break;
      case 'r':
        if(debug) { console.log('    Remove: ' + JSON.stringify(m.v)); }
        BoardGems.removeGem(board, m.v.x, m.v.y);
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

  function applyMutations(board, segments, idx) {
    var segmentIndex;
    if(idx === undefined) {
      segmentIndex = 0;
    } else {
      segmentIndex = idx;
    }
    if(debug) {
      var count = _.reduce(segments, function(i, c){ return i + c.length; }, 0);
      console.log('Processing [' + segments.length + '] segments containing [' + count + '] mutations.');
    }
    if(segments.length > 0) {
      applySegment(board, segments[0], segmentIndex);
      var f = function() {
        applyMutations(board, segments.splice(1), segmentIndex + 1);
      };
      setTimeout(f, 200);
    }
  }

  return {
    applyMutations: applyMutations
  };
});
