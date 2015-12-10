/* global define:false */
/* global Phaser:false */
/* global _:false */
define(['board/Board', 'gem/Gem', 'playmat/PlaymatResizer', 'utils/Status'], function (Board, Gem, PlaymatResizer, Status) {
  'use strict';

  var Playmat = function(game) {
    Phaser.Group.call(this, game, null, 'playmat');
    this.game.add.existing(this);

    this.players = {};

    this.resizer = new PlaymatResizer(this);
  };

  Playmat.prototype = Object.create(Phaser.Group.prototype);
  Playmat.prototype.constructor = Playmat;

  Playmat.prototype.setBrawl = function(brawl) {
    if(this.brawl !== undefined) {
      throw 'Already using brawl [' + this.brawl.id + '].';
    }
    this.brawl = brawl;
    Status.setScenario(brawl.scenario);
    var playmat = this;
    var style = { font: '60px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
    _.each(brawl.players, function(p) {
      var board = new Board(p.board, playmat.game);
      playmat.add(board);

      var nameLabel = new Phaser.Text(playmat.game, 0, 0, p.name, style);
      playmat.add(nameLabel);

      var scoreLabel = new Phaser.Text(playmat.game, 0, 0, p.score, style);
      scoreLabel.anchor.set(1, 0);
      playmat.add(scoreLabel);

      playmat.players[p.id] = {
        id: p.id,
        name: p.name,
        nameLabel: nameLabel,
        score: p.score,
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
