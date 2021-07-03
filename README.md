# Store Runner

## An application to simulate a store managing experience

#### Proposal:

The Store Runner App is a perfect choice for those who want to experience what it is like to run a simple store.  

At the start, the user would have an empty store with a default initial asset.
There would be a number of default items, with default costs and unset retail prices, provided to the user.  

The user would first want to **add items to the store's stock**.
To do so successfully, the user needs to make sure the current asset could afford this transaction.
  
Before **selling an item**, the user should make sure that:
1. *There is a sufficient quantity of the item in stock*;  
2. *The retail price of the item has been set*.

The user could also **change the retail price** of an item given that it has been stocked.  

Furthermore, the user is able to track the **total expenditure** (which comes from stocking items) 
and **total revenue** (which comes from selling items), as well as the **profit** made so far.

#### Reason of Interest:

As I was scanning around my room seeking inspirations for my project ideas, 
I see that every single item I own is bought from a store. A thought came to me that
instead of a customer who buys, I would like to experience the life of a store's owner who sells. 

Thus, I am designing the Store Runner application to help those with a similar desire.



## User Stories

- As a user, I want to be able to add an item to the store's stock.
- As a user, I want to be able to set the retail price of an item.
- As a user, I want to be able to sell an item.
- As a user, I want to be able to find the quantity of an item in stock.
- As a user, I want to be able to track my expenditure and revenue.
- As a user, I want to be able to save my store to file.
- As a user, I want to be able to load my store from file.


### Phase 4: Task 2
Made Store class more robust by adding an exception in sell() method.

### Phase 4: Task 3
I would refactor some methods in the StoreGUI class such that 
it is divided into 3 classes: 
1. one class dealing with the menu, 
2. one appending the images,  and 
3. one forming the window that will be run. 

Also, I might remove the associations between StoreGUI and the collection of Items. 
Since StoreGUI is associated with Store, it already has access to a collection of Items.