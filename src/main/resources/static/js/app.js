document.addEventListener('DOMContentLoaded', function () {
  const token = localStorage.getItem('token');
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const registerForm = document.getElementById('registerForm');
  const loginForm = document.getElementById('loginForm');
  const addSkillForm = document.getElementById('addSkillForm');
  const requestForm = document.getElementById('requestForm');
  const feedbackForm = document.getElementById('feedbackForm');

  if (registerForm) {
    registerForm.onsubmit = async (e) => {
      e.preventDefault();
      const formData = Object.fromEntries(new FormData(registerForm));
      const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers,
        body: JSON.stringify(formData)
      });

	  const result = await res.json();
	  document.getElementById("messageBox").innerText = result.message || "Registered successfully!";
	  document.getElementById("messageBox").classList.remove("d-none");
    };
  }


  if (loginForm) {
    loginForm.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(loginForm));
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers,
        body: JSON.stringify(data)
      });

      if (res.ok) {
        alert(await res.text());
        window.location.href = 'dashboard.html';
      } else {
        alert(await res.text()); // show error from backend
      }
    };
  }


  if (addSkillForm) {
    addSkillForm.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(addSkillForm));
      const res = await fetch('/api/user/skills/add', {
        method: 'POST', headers, body: JSON.stringify(data)
      });
      alert('Skill added!');
    };
  }

  if (requestForm) {
    requestForm.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(requestForm));
      const res = await fetch('/api/request/send', {
        method: 'POST', headers, body: JSON.stringify(data)
      });
      alert('Request sent.');
    };
  }

  if (feedbackForm) {
    feedbackForm.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(feedbackForm));
      data.rating = parseInt(data.rating);
      const res = await fetch('/api/feedback/giverating', {
        method: 'POST', headers, body: JSON.stringify(data)
      });
      alert('Feedback submitted!');
    };
  }

  const matchList = document.getElementById('matchList');
  if (matchList) {
    fetch('/api/match/top', { headers })
      .then(res => res.json())
      .then(users => {
        users.forEach(user => {
          const li = document.createElement('li');
          li.className = 'list-group-item';
          li.innerText = `${user.username} (${user.location})`;
          matchList.appendChild(li);
        });
      });
  }

  window.fetchUsers = async function () {
    const res = await fetch('/api/admin/users', { headers });
    const users = await res.json();
    const list = document.getElementById('userList');
    list.innerHTML = '';
    users.forEach(u => {
      const li = document.createElement('li');
      li.className = 'list-group-item d-flex justify-content-between';
      li.innerHTML = `${u.username} <span>${u.role}</span>`;
      list.appendChild(li);
    });
  };
  
  const forgotForm = document.getElementById('forgotPasswordForm');
  const resetForm = document.getElementById('resetPasswordForm');
  const statusForm = document.getElementById('statusForm');

  if (forgotForm) {
    forgotForm.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(forgotForm));
      const res = await fetch('/api/auth/forgot-password', {
        method: 'POST', headers, body: JSON.stringify(data)
      });
      const msg = await res.text();
      document.getElementById("forgotMessage").innerText = msg;
    };
  }

  if (resetForm) {
    resetForm.onsubmit = async (e) => {
      e.preventDefault();
      const data = Object.fromEntries(new FormData(resetForm));
      const res = await fetch('/api/auth/reset-password', {
        method: 'POST', headers, body: JSON.stringify(data)
      });
      const msg = await res.text();
      document.getElementById("resetMessage").innerText = msg;
    };
  }

  if (statusForm) {
    statusForm.onsubmit = async (e) => {
      e.preventDefault();
      const form = new FormData(statusForm);
      const id = form.get("requestId");
      const status = form.get("status");
      const link = form.get("meetLink");
      const url = `/api/request/${id}/respond?status=${status}${link ? `&meetLink=${encodeURIComponent(link)}` : ''}`;
      const res = await fetch(url, { method: 'POST', headers });
      const msg = await res.text();
      document.getElementById("statusMessage").innerText = msg;
    };
  }

});