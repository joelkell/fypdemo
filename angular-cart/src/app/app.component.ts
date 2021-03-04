import { Component } from '@angular/core';

@Component({
  selector: '[id="cart-container"]',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'angular-cart';
  cart;

  getToken = () => {
    const tokenString = localStorage.getItem('token');
    const userToken = JSON.parse(tokenString);
    let currentTime = new Date();
    if (userToken) {
      if (currentTime.getTime() < Date.parse(userToken.expires_in)) {
        return userToken;
      } else {
        return null;
      }
    } else {
      return null;
    }
  };

  loggedIn = this.getToken();

  getProduct = async (productId) => {
    const response = await fetch(`http://localhost:8080/products/${productId}`);
    const data = await response.json();
    return data;
  };

  updateCart = () => {
    let userToken = this.getToken();
    if (userToken) {
      let authToken = userToken.token_type + ' ' + userToken.access_token;
      fetch(`http://localhost:8080/carts/${userToken.username}`, {
        method: 'PUT',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authorization: authToken,
        },
        body: JSON.stringify({
          _id: this.cart._id,
          userId: userToken.username,
          deliveryPrice: 0,
          totalPrice: 0,
        }),
      }).then((response) => {
        response.json().then((data) => {
          alert('Order Created!');
          this.cart = data;
        });
      });
    } else {
      alert('not logged in!');
    }
  };

  createOrder = () => {
    let userToken = this.getToken();
    if (userToken) {
      let authToken = userToken.token_type + ' ' + userToken.access_token;
      fetch(`http://localhost:8080/orders/`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authorization: authToken,
        },
        body: JSON.stringify({
          userId: userToken.username,
          status: 'ORDERED',
          totalPrice: this.cart.totalPrice,
          orderItems: this.cart.cartItems,
        }),
      }).then((response) => {
        response.json().then((data) => {
          this.updateCart();
        });
      });
    } else {
      alert('not logged in!');
    }
  };

  removeItemFromCart = async (itemIndex, productId, quantity) => {
    let cartItems = this.cart.cartItems;
    cartItems.splice(itemIndex, 1);
    let product = await this.getProduct(productId);

    //TODO add stock level back up

    let userToken = this.getToken();
    if (userToken) {
      let authToken = userToken.token_type + ' ' + userToken.access_token;
      fetch(`http://localhost:8080/carts/${userToken.username}`, {
        method: 'PUT',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authorization: authToken,
        },
        body: JSON.stringify({
          _id: this.cart._id,
          userId: userToken.username,
          deliveryPrice: 0,
          totalPrice: this.cart.totalPrice - product.price * quantity,
          cartItems: cartItems,
        }),
      }).then((response) => {
        response.json().then(async (data) => {
          console.log('item removed!');
          if (data.cartItems) {
            await data.cartItems.forEach(async (cartItem) => {
              let product = await this.getProduct(cartItem.productId);
              cartItem.name = product.name;
            });
          }
          this.cart = data;
        });
      });
    } else {
      alert('not logged in!');
    }
  };

  ngOnInit() {
    window.addEventListener(
      'addToCart',
      (event) => {
        if ((<any>event).detail.cartItems) {
          (<any>event).detail.cartItems.forEach(async (cartItem) => {
            let product = await this.getProduct(cartItem.productId);
            cartItem.name = product.name;
          });
        }
        this.cart = (<any>event).detail;
      },
      false
    );

    let userToken = this.getToken();
    if (userToken) {
      let authToken = userToken.token_type + ' ' + userToken.access_token;
      fetch(`http://localhost:8080/carts/${userToken.username}`, {
        method: 'GET',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authorization: authToken,
        },
      }).then((response) => {
        response.json().then((data) => {
          if (data.cartItems) {
            data.cartItems.forEach(async (cartItem) => {
              let product = await this.getProduct(cartItem.productId);
              cartItem.name = product.name;
            });
          }
          this.cart = data;
        });
      });
    }
  }
}
