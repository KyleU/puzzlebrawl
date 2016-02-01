/* global define:false */
define([], function() {
  'use strict';

  function Matchmaking(game) {
    this.game = game;
    this.queueScenario = document.getElementById('queue-scenario');
    this.queuePlayerCount = document.getElementById('queue-player-count');
    this.queueList = document.getElementById('brawl-queue');
  }

  Matchmaking.prototype.show = function(msg) {
    this.game.navigation.navigate('matchmaking');
    this.queueScenario.innerText = msg.scenario;
    this.queuePlayerCount.innerText = msg.requiredPlayers;
    while (this.queueList.hasChildNodes()) {
      this.queueList.removeChild(this.queueList.lastChild);
    }
    for(var playerIdx = 0; playerIdx < msg.players.length; playerIdx++) {
      var player = msg.players[playerIdx];
      var entry = document.createElement('li');
      entry.appendChild(document.createTextNode(player));
      this.queueList.appendChild(entry);
    }
  };

  return Matchmaking;
});
