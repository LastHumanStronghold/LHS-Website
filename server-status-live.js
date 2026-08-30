(() => {
  const API = 'https://api.mcsrvstat.us/3/144.31.136.75:25566';
  const status = document.getElementById('server-status');
  if (!status) return;
  const style = document.createElement('style');
  style.textContent = '.server-status-inner.online{border-color:rgba(75,190,105,.18)!important;background:rgba(75,190,105,.025)!important}.server-status-inner.offline{border-color:rgba(255,138,31,.13)!important}.server-status-inner b{display:block;color:#d9d4cf;font-size:12px;font-weight:700}.server-status-inner small{display:block;margin-top:1px;color:#706962;font-size:10px}.server-dot{display:inline-block!important;width:7px!important;height:7px!important;flex:0 0 7px!important;border-radius:50%!important;background:#ff8a1f!important}.server-status-inner.online .server-dot{background:#62c77b!important;box-shadow:0 0 10px rgba(98,199,123,.55);animation:serverPulse 2.2s ease-in-out infinite}@keyframes serverPulse{0%,100%{opacity:.65;transform:scale(.9)}50%{opacity:1;transform:scale(1.15)}}@media(prefers-reduced-motion:reduce){.server-status-inner.online .server-dot{animation:none!important}}';
  document.head.appendChild(style);
  const setStatus = (online, players = 0, max = 0) => {
    status.classList.toggle('online', online);
    status.classList.toggle('offline', !online);
    const word = players === 1 ? 'игрок' : players >= 2 && players <= 4 ? 'игрока' : 'игроков';
    status.innerHTML = online
      ? `<span class="server-dot"></span><span><b>Сервер онлайн</b><small>${players} ${word} на сервере${max ? ` · ${max} мест` : ''}</small></span>`
      : '<span class="server-dot"></span><span><b>Сервер офлайн</b><small>Сейчас сервер недоступен</small></span>';
  };
  const check = async () => {
    try {
      const response = await fetch(`${API}?_=${Date.now()}`, { cache: 'no-store' });
      if (!response.ok) throw new Error('status');
      const data = await response.json();
      setStatus(Boolean(data.online), data.players?.online || 0, data.players?.max || 0);
    } catch { setStatus(false); }
  };
  check();
  setInterval(check, 30000);
})();
