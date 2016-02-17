/* global define:false */
/* global _:false */
define(['player/PlayerGroup', 'playmat/PlaymatInput', 'utils/Status'], function (PlayerGroup, PlaymatInput, Status) {
  'use strict';

  function startBrawl(playmat, self, brawl) {
    if(playmat.brawl !== undefined && playmat.brawl !== null) {
      throw new Error('Already using brawl [' + playmat.brawl.id + '].');
    }
    playmat.self = self;
    playmat.brawl = brawl;
    Status.setStatus(brawl.scenario);

    playmat.game.gameInput.resume();
    playmat.input = new PlaymatInput(playmat);

    var selfPlayer = _.find(brawl.players, function(pl) {
      return pl.id === self;
    });

    _.each(brawl.players, function(pl) {
      var score = pl.score;
      if(score === undefined) {
        score = 0;
      }

      var role = (pl.id === self) ? 'self' : (selfPlayer.team === pl.team) ? 'friend' : 'enemy';
      var playerGroup = new PlayerGroup(playmat.game, pl, role, score);
      playmat.add(playerGroup);

      if(pl.id === self) {
        playmat.selfBoard = playerGroup.board;
      }

      playmat.players[pl.id] = {
        id: pl.id,
        name: pl.name,
        team: pl.team,
        score: score,
        group: playerGroup,
        target: pl.target
      };
    });

    playmat.otherPlayers = _.filter(playmat.players, function(p) {
      return p.id !== playmat.self;
    });

    playmat.layout.refreshLayout();
    playmat.targets.refreshTarget();
  }

  function resignBrawl(playmat) {
    Status.setStatus('Brawl Complete');
    _.each(playmat.players, function(pl) {
      playmat.remove(pl.group);
      pl.group.destroy();
    });
    if(playmat.targets !== undefined && playmat.targets !== null) {
      playmat.targets.destroy();
    }
    var id = playmat.brawl.id;
    playmat.self = null;
    playmat.players = {};
    playmat.brawl = null;
    playmat.game.send('ResignBrawl', { id: id });
  }

  return {
    startBrawl: startBrawl,
    resignBrawl: resignBrawl
  };
});
