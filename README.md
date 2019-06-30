# RoomBooker
Coding Interview Question

You are given a dataset of room inventory. These are the rooms of a hotel with attributes such as the price for 1 week and how many occupants/guests the room accommodates.
There is a min and max guest number which means the room will take no less than the min and no more than the max number of guests. Some rooms are priced per person (pp)
and some are priced per unit (pu). To calculate the per person price from the per unit price you have to divide the pu price by the number of guests you are putting in the room.
A typical large hotel booking might include more guests than any single room that can accommodate. There can be many room combinations that will accommodate large numbers
of guests with each unique combination having a different price. As an example 7 guests will fit into two family rooms but they can also fit into one family room and one twin triple
or 4 twin rooms. But which combination is cheaper?
This is a test of your problem solving and analytical approach to coding, so the more efficient your code is the better. Your code should execute and have tests for what you are
trying to accomplish and how you expect the results to be. Program the algorithm that you feel would best work on this data structure to produce the first 3 cheapest (price) room
combinations for 7 guests. You can supply a zipped copy of your code or github link.

Hints:
1. You need to take into account the pricing model.
2. Also bonus points for use of lambdas and streams and additional efficiency steps.
3. There are 55 rows in total and only a sample is shown.
4. Each reference is a piece of inventory.
5. There is an expectation that your code reflects the quality you aspire to.

Totals: Double x20, Twin x20, Twin/Tripple x10, Family x5
