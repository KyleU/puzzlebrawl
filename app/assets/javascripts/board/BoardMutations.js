/* global define:false */
/* global _:false */
define([], function () {
  'use strict';

  var debug = true;

  return {
    applyMutations: function(board, mutations) {
      if(debug) {
        var count = _.reduce(mutations, function(i, c){ return i + c.length; }, 0);
        console.log('Processing [' + mutations.length + '] sets containing [' + count + '] mutations.');
      }
      _.each(mutations, function(mSeq, mIdx) {
        if(debug) { console.log('  Set [' + mIdx + '] (' + mSeq.length + ' mutations):'); }
        _.each(mSeq, function(m) {
          switch(m.t) {
            case 'a':
              if(debug) { console.log('    Add: ' + JSON.stringify(m.v)); }
              board.addGem(m.v.gem, m.v.x, m.v.y);
              break;
            case 'g':
              if(debug) { console.log('    SetActive: ' + JSON.stringify(m.v)); }
              board.setActiveGems(m.v.gems);
              break;
            case 'm':
              if(debug) { console.log('    Move: ' + JSON.stringify(m.v)); }
              board.moveGem(m.v.x, m.v.y, m.v.xDelta, m.v.yDelta);
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
        });
      });
    }
  };
});
