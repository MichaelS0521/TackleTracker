// import './websiteLogin.scss';

// function login() {
//     var username = document.getElementById("username").value;
//     var password = document.getElementById("password").value;

//     console.log("Login button clicked");

// fetch('/api/authenticate', {
//     method: 'POST',
//     headers: {
//         'Content-Type': 'application/json',
//     },
//     body: JSON.stringify({
//         username: username,
//         password: password
//     })
// })
// .then(response => {
//     if (!response.ok) {
//         throw new Error('Network response was not ok');
//     }
//     return response.json();
// })
// .then(data => {
//     console.log(data);

//     window.location.href = "/website.html";
// })
// .catch(error => {
//     console.error('There was a problem with the login:', error);

//     alert('Wrong username or password. Please try again.');
// });
// }

// export default login();
