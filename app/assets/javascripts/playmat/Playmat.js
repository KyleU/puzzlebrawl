/* global define:false */
/* global Phaser:false */
/* global _:false */
define([
  'board/Board', 'gem/Gem',
  'playmat/PlaymatInput', 'playmat/PlaymatLabels', 'playmat/PlaymatLayout', 'playmat/PlaymatResizer', 'playmat/PlaymatTargets',
  'utils/Formatter', 'utils/Status'
], function (Board, Gem, PlaymatInput, PlaymatLabels, PlaymatLayout, PlaymatResizer, PlaymatTargets, Formatter, Status) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.players = {};
    this.self = null;

    this.layout = new PlaymatLayout(this);
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
    this.input = new PlaymatInput(this);

    var playmat = this;
    _.each(brawl.players, function(p) {
      var board = new Board(p.id, p.board, playmat);
      playmat.add(board);

      var score = p.score;
      if(score === undefined) {
        score = 0;
      }

      playmat.players[p.id] = {
        id: p.id,
        name: p.name,
        team: p.team,
        score: score,
        board: board,
        target: p.target
      };
    });

    _.each(playmat.players, function(p) {
      p.labels = new PlaymatLabels(playmat, p.id, p.name, p.score);
    });

    playmat.otherPlayers = _.filter(playmat.players, function(p) {
      return p.id !== playmat.self;
    });

    playmat.layout.refreshLayout();
    playmat.targets.refreshTarget();
  };

  Playmat.prototype.changeScore = function(id, delta) {
    var player = this.players[id];
    player.score += delta;
    player.labels.scoreLabel.text = Formatter.withCommas(player.score);
  };

  return Playmat;
});
