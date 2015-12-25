/* global define:false */
/* global Phaser:false */
/* global _:false */
define([
  'board/Board',
  'gem/Gem',
  'playmat/PlaymatInput',
  'playmat/PlaymatResizer',
  'utils/Status'
], function (Board, Gem, PlaymatInput, PlaymatResizer, Status) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.players = {};
    this.self = null;

    this.input = new PlaymatInput(this);
    this.resizer = new PlaymatResizer(this);
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
    var style = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
    _.each(brawl.players, function(p) {
      var board = new Board(p.id, p.board, playmat);
      playmat.add(board);

      var nameLabel = new Phaser.Text(playmat.game, 0, 0, p.name, style);
      nameLabel.name = 'name-label-' + p.name;
      playmat.add(nameLabel);

      var score = p.score;
      if(score === undefined) {
        score = 0;
      }

      var scoreLabel = new Phaser.Text(playmat.game, 0, 0, score, style);
      scoreLabel.name = 'score-label-' + p.name;
      scoreLabel.anchor.set(1, 0);
      playmat.add(scoreLabel);

      playmat.players[p.id] = {
        id: p.id,
        name: p.name,
        nameLabel: nameLabel,
        score: score,
        scoreLabel: scoreLabel,
        board: board
      };
    });
    this.resizer.refreshLayout();
  };

  Playmat.prototype.changeScore = function(id, delta) {
    var player = this.players[id];
    player.score += delta;
    player.scoreLabel.text = player.score;
  };

  return Playmat;
});
