#include "StandardFull.h"
#include "StandardSeller.h"
#include "StandardBuyer.h"
#include "User.h"

StandardFull::StandardFull(string userName): User(userName), StandardSeller(userName), StandardBuyer(userName){}