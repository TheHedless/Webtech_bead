const API_BASE = 'http://localhost:9090';

        function showStatus(elementId, message, type) {
            const el = document.getElementById(elementId);
            el.textContent = message;
            el.className = `status ${type}`;
        }

        function showResponse(data) {
            const responseEl = document.getElementById('response');
            const contentEl = document.getElementById('responseContent');
            contentEl.textContent = JSON.stringify(data, null, 2);
            responseEl.style.display = 'block';
        }

        function updateTokenInfo() {
            const token = localStorage.getItem('token');
            const tokenInfo = document.getElementById('tokenInfo');
            if (!tokenInfo) return;
            if (token) {
                tokenInfo.innerHTML = `
                    <p><strong>Logged in</strong></p>
                `;
                tokenInfo.classList.add('show');
            } else {
                tokenInfo.classList.remove('show');
            }
        }

        async function register() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;

            if (!username || !password || !name || !email) {
                showStatus('authStatus', 'Please fill all fields', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/auth/register`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, password, name, email })
                });

                if (response.ok) {
                    showStatus('authStatus', 'Registration successful!', 'success');
                    showResponse({ message: 'Registration successful' });
                } else {
                    const error = await response.json();
                    showStatus('authStatus', `Registration failed: ${JSON.stringify(error.errors || error.message)}`, 'error');
                    showResponse(error);
                }
            } catch (error) {
                showStatus('authStatus', `Error: ${error.message}`, 'error');
            }
        }

        async function login() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            if (!username || !password) {
                showStatus('authStatus', 'Please enter username and password', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/auth/login`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, password })
                });

                const token = await response.text();

                if (response.ok && token) {
                    localStorage.setItem('token', token);
                    showStatus('authStatus', 'Login successful!', 'success');
                    updateTokenInfo();
                    showResponse({ message: 'Login successful', token: token });
                } else {
                    showStatus('authStatus', 'Login failed', 'error');
                    showResponse({ error: 'Invalid credentials' });
                }
            } catch (error) {
                showStatus('authStatus', `Error: ${error.message}`, 'error');
            }
        }

        async function makeAuthRequest(method, endpoint, body = null) {
            const token = localStorage.getItem('token');

            if (!token) {
                showStatus('apiStatus', 'Please login first', 'error');
                return null;
            }

            try {
                const options = {
                    method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                };

                if (body) options.body = JSON.stringify(body);

                const response = await fetch(`${API_BASE}${endpoint}`, options);

                if (response.ok) {
                    const data = await response.json();
                    showStatus('apiStatus', 'Request successful', 'success');
                    showResponse(data);
                    return data;
                } else if (response.status === 403) {
                    showStatus('apiStatus', 'Access denied (403)', 'error');
                    showResponse({ error: 'Access denied' });
                } else {
                    const error = await response.json();
                    showStatus('apiStatus', `Error: ${response.status}`, 'error');
                    showResponse(error);
                }
            } catch (error) {
                showStatus('apiStatus', `Error: ${error.message}`, 'error');
            }
        }

        function getByName() {
            const name = document.getElementById('getByNameInput').value;
            if (!name) {
                showStatus('apiStatus', 'Please enter a name', 'error');
                return;
            }
            makeAuthRequest('GET', `/api/ser/byName/${encodeURIComponent(name)}`);
        }

        function getById() {
            const id = document.getElementById('getByIdInput').value;
            if (!id) {
                showStatus('apiStatus', 'Please enter an ID', 'error');
                return;
            }
            makeAuthRequest('GET', `/api/ser/byId?id=${id}`);
        }

        function getAll() {
            makeAuthRequest('GET', `/api/ser/all?limit=10`);
        }

        function getAllByUser() {
            makeAuthRequest('GET', `/api/ser/byUser?limit=10`);
        }

        async function deleteByUser() {
            const id = document.getElementById('deleteByUserIdInput').value;
            if (!id) {
                showStatus('apiStatus', 'Please enter an ID to delete', 'error');
                return;
            }

            const token = localStorage.getItem('token');
            if (!token) {
                showStatus('apiStatus', 'Please login first', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/api/ser/deleteByUser?id=${encodeURIComponent(id)}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const result = await response.json();
                    showStatus('apiStatus', result.message, 'success');
                    showResponse(result);
                } else {
                    const err = await response.json();
                    showStatus('apiStatus', err.message || `Error: ${response.status}`, 'error');
                    showResponse(err);
                }
            } catch (error) {
                showStatus('apiStatus', `Error: ${error.message}`, 'error');
            }
        }

        async function deleteByIdUser() {
            const id = document.getElementById('deleteByUserIdInput').value;
            if (!id) {
                showStatus('apiStatus', 'Please enter an ID to delete', 'error');
                return;
            }

            const token = localStorage.getItem('token');
            if (!token) {
                showStatus('apiStatus', 'Please login first', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/api/ser/delete?id=${encodeURIComponent(id)}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    showStatus('apiStatus', 'Beer deleted successfully', 'success');
                    showResponse({ message: 'Beer deleted successfully' });
                } else if (response.status === 403) {
                    const userResponse = await fetch(`${API_BASE}/api/ser/deleteByUser?id=${encodeURIComponent(id)}`, {
                        method: 'DELETE',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (userResponse.ok) {
                        const result = await userResponse.json();
                        showStatus('apiStatus', result.message, 'success');
                        showResponse(result);
                    } else {
                        const err = await userResponse.json();
                        showStatus('apiStatus', err.message || 'Error: ' + userResponse.status, 'error');
                        showResponse(err);
                    }
                } else {
                    const err = await response.json();
                    showStatus('apiStatus', err.message || 'Error: ' + response.status, 'error');
                    showResponse(err);
                }
            } catch (error) {
                showStatus('apiStatus', 'Error: ' + error.message, 'error');
            }
        }

        async function deleteById() {
            const id = document.getElementById('deleteByIdInput').value;
            if (!id) {
                showStatus('apiStatus', 'Please enter an ID to delete', 'error');
                return;
            }

            const token = localStorage.getItem('token');
            if (!token) {
                showStatus('apiStatus', 'Please login first', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/api/ser/deleteByUser?id=${encodeURIComponent(id)}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const result = await response.json();
                    showStatus('apiStatus', result.message, 'success');
                    showResponse(result);
                } else {
                    const err = await response.json();
                    showStatus('apiStatus', err.message || 'Error: ' + response.status, 'error');
                    showResponse(err);
                }
            } catch (error) {
                showStatus('apiStatus', 'Error: ' + error.message, 'error');
            }
        }

        function save() {
            const name = document.getElementById('saveName').value;
            if (!name) {
                showStatus('apiStatus', 'Please enter a name', 'error');
                return;
            }

            const data = { name };

            const additionalData = document.getElementById('saveData').value;
            if (additionalData) {
                try {
                    Object.assign(data, JSON.parse(additionalData));
                } catch (e) {
                    showStatus('apiStatus', 'Invalid JSON', 'error');
                    return;
                }
            }

            makeAuthRequest('POST', '/api/ser/save', data);
        }

        async function grantAdmin() {
            const username = document.getElementById('adminUsername').value;
            if (!username) {
                showStatus('adminStatus', 'Please enter a username', 'error');
                return;
            }
            
            const token = localStorage.getItem('token');
            if (!token) {
                showStatus('adminStatus', 'Please login first (must be ADMIN)', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/api/felhasznalo/grant-admin?username=${encodeURIComponent(username)}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const user = await response.json();
                    showStatus('adminStatus', `ADMIN granted to ${username}`, 'success');
                    showResponse(user);
                } else if (response.status === 403) {
                    showStatus('adminStatus', 'Access denied - you need ADMIN role', 'error');
                    showResponse({ error: 'Access denied' });
                } else {
                    const error = await response.json();
                    showStatus('adminStatus', `Error: ${response.status}`, 'error');
                    showResponse(error);
                }
            } catch (error) {
                showStatus('adminStatus', `Error: ${error.message}`, 'error');
            }
        }

        async function revokeAdmin() {
            const username = document.getElementById('adminUsername').value;
            if (!username) {
                showStatus('adminStatus', 'Please enter a username', 'error');
                return;
            }
            
            const token = localStorage.getItem('token');
            if (!token) {
                showStatus('adminStatus', 'Please login first (must be ADMIN)', 'error');
                return;
            }

            try {
                const response = await fetch(`${API_BASE}/api/felhasznalo/revoke-admin?username=${encodeURIComponent(username)}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const user = await response.json();
                    showStatus('adminStatus', `ADMIN revoked from ${username}`, 'success');
                    showResponse(user);
                } else if (response.status === 403) {
                    showStatus('adminStatus', 'Access denied - you need ADMIN role', 'error');
                    showResponse({ error: 'Access denied' });
                } else {
                    const error = await response.json();
                    showStatus('adminStatus', `Error: ${response.status}`, 'error');
                    showResponse(error);
                }
            } catch (error) {
                showStatus('adminStatus', `Error: ${error.message}`, 'error');
            }
        }

        updateTokenInfo();