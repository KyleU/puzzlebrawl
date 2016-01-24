/* global define:false */
/* global _:false */
define(['board/Board', 'playmat/PlaymatInput', 'playmat/PlaymatLabels', 'utils/Status'], function (Board, PlaymatInput, PlaymatLabels, Status) {
  'use strict';

  function startBrawl(playmat, self, brawl) {
    if(playmat.brawl !== undefined && playmat.brawl !== null) {
      throw 'Already using brawl [' + playmat.brawl.id + '].';
    }
    playmat.self = self;
    playmat.brawl = brawl;
    Status.setScenario(brawl.scenario);
    playmat.input = new PlaymatInput(playmat);

    _.each(brawl.players, function(pl) {
      var board = new Board(pl.id, pl.board, playmat);
      playmat.add(board);

      if(pl.id === playmat.self) {
        playmat.selfBoard = board;
      }

      var score = pl.score;
      if(score === undefined) {
        score = 0;
      }

      playmat.players[pl.id] = {
        id: pl.id,
        name: pl.name,
        team: pl.team,
        score: score,
        board: board,
        target: pl.target
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
  }

  function resignBrawl(playmat) {
    Status.setScenario('Brawl Complete');
    _.each(playmat.players, function(pl) {
      playmat.remove(pl.board);
      pl.labels.destroy();
      pl.board.destroy();
    });
    if(playmat.targets !== undefined && playmat.targets !== null) {
      playmat.targets.destroy();
    }
    playmat.self = null;
    playmat.players = {};
    playmat.brawl = null;
  }

  return {
    startBrawl: startBrawl,
    resignBrawl: resignBrawl
  };
});