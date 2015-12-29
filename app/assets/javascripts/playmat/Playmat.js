/* global define:false */
/* global Phaser:false */
/* global _:false */
define([
  'board/Board', 'gem/Gem', 'playmat/PlaymatInput', 'playmat/PlaymatResizer', 'playmat/PlaymatTargets', 'utils/Status'
], function (Board, Gem, PlaymatInput, PlaymatResizer, PlaymatTargets, Status) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.players = {};
    this.self = null;

    this.input = new PlaymatInput(this);
    this.resizer = new PlaymatResizer(this);
    this.targets = new PlaymatTargets(this);
  };

  Playmat.prototype = Object.create(Phaser.Group.prototype);
  Playmat.prototype.constructor = Playmat;

  Playmat.prototype.setBrawl = function(self, brawl) {
    if(this.brawl !== undefined) {
      throw 'Already using brawl [' + this.brawl.id + '].';
    }
    this.self = self;
    this.brawl = brawl;
    Status.setScenario(brawl.scenario);
    var playmat = this;
    _.each(brawl.players, function(p) {
      var score = p.score;
      if(score === undefined) {
        score = 0;
      }

      var board = new Board(p.id, p.board, p.name, score, playmat);
      playmat.add(board);

      playmat.players[p.id] = {
        id: p.id,
        name: p.name,
        board: board,
        target: p.target
      };
    });

    playmat.otherPlayers = _.filter(playmat.players, function(p) {
      return p.id !== playmat.self;
    });

    playmat.resizer.refreshLayout();
    playmat.targets.refreshTarget();
  };

  return Playmat;
});
