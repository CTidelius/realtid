realtid 
=======

EDA040 Realtidsprogrammering

Protocol:
TCP

Push/pull?:
Push från server till klient

Movie/idle switch:
På klientsidan: 2 trådar på en socket. Ena tråden sköter ett broadcastat meddelande från monitorn som säger åt servrar att byta mode. Andra tråden tar emot bilder
På serversidan: 2 trådar på en socket. Ena tråden sköter att ta emot det broadcastade meddelandet och ändrar mode på serversidan i en monitor. Andra tråden skickar bilder.

Synch/osynch:

När man connectar tar man lokal tid i klient. Frågar server om tid och får ett svar. Tiden det tog för svar genom 2 kommer ge pingen och m.h.a. den kan man räkna ut tidsskillnaden mellan klient och server. När servern sedan skickar bilder med timestamps jämför man tiden bilden skickades med tiden bilden togs emot för att räkna ut delay och då jämföra med andra bilder från andra servers. Blir skillnad större än 0.2s där vet man att man måste gå till osynch-mode. Annars sköter DisplayHandler synkronisering eftersom att den vet vilken tid bilderna togs. 
