(() => {
  const API = 'https://api.mcsrvstat.us/3/144.31.136.75:25566';
  const status = document.getElementById('server-status');
  if (!status) return;

  const setStatus = (online, players = 0, max = 0) => {
    status.classList.toggle('online', online);
    status.classList.toggle('offline', !online);
    status.innerHTML = online
      ? `<span class="server-dot"></span><span><b>Сервер онлайн</b><small>${players} ${players === 1 ? 'игрок' : players >= 2 && players <= 4 ? 'игрока' : 'игроков'} на сервере${max ? ` · ${max} мест` : ''}</small></span>`
      : `<span class="server-dot"></span><span><b>Сервер офлайн</b><small>Сейчас сервер недоступен</small></span>`;
  };

  const check = async () => {
    try {
      const response = await fetch(`${API}?_=${Date.now()}`, { cache: 'no-store' });
      if (!response.ok) throw new Error('status');
      const data = await response.json();
      setStatus(Boolean(data.online), data.players?.online || 0, data.players?.max || 0);
    } catch {
      setStatus(false);
    }
  };

  check();
  setInterval(check, 30000);
})();
