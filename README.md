# stock_market_auto_trading_engine
Crawl, extract, aggregate market data hourly. Makes predefined buys/sells on tradeking using heuristic.s 

#1. script1(run as cron job every x hours): run all scripts below
#2. script2: crawl google finance and save html
#3. script3: process html, extract, save {ticker, time, date, eps, price ....etc}
#4. script4: aggergate data and preprocess variables for the algo
#5. script5: algo script for making buy/sell triggers
#6. script6: make a buy or sell based on alog scrip output stats
