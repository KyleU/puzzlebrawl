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
      case 't':
        if(debug) { console.log('    Target: ' + JSON.stringify(m.v)); }
        board.game.playmat.targets.setTarget(board.owner, m.v.t);
        break;
      default:
        console.log('Unhandled mutation [' + m.t + '].');
        break;
    }
  }

  function applySegment(board, segment, idx, scoreCallback) {
    if(debug) { console.log('  Segment [' + idx + ': ' + segment.category + '] (' + segment.mutations.length + ' mutations):'); }

    _.each(segment.mutations, function(mutation) {
      applyMutation(board, mutation);
    });

    var sd = segment.scoreDelta;
    if(sd !== undefined) {
      if(sd instanceof Array) {
        sd = sd[0];
      }
      scoreCallback(sd);
    }
  }

  function applyMutations(board, segments, scoreCallback, idx) {
    if(segments.length > 0) {
      if(debug) {
        var count = _.reduce(segments.slice(1), function(i, c) { return i + c.mutations.length; }, 0);
        var additional;
        if(segments.length === 1) {
          additional = ', no work remains.';
        } else {
          additional = ', leaving [' + (segments.length - 1) + '] segments remaining, containing [' + count + '] mutations.';
        }
        console.log('Processing one segment of length [' + segments[0].mutations.length + ']' + additional);
      }
      if(board.owner === board.playmat.self) {
        board.game.isTweening = true;
      }
      var s = segments[0];
      applySegment(board, s, idx, scoreCallback);
      if(segments.length === 1 && board.owner === board.playmat.self) {
        board.game.isTweening = false;
      }
      if(segments.length > 1) {
        var f = function() {
          applyMutations(board, segments.splice(1), scoreCallback, idx + 1);
        };
        setTimeout(f, 200);
      }
    }
  }

  return {
    applyMutations: applyMutations
  };
});
