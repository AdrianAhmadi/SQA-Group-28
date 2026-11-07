#pragma once
#include "StandardBuyer.h"
#include "StandardSeller.h"

class StandardFull : public StandardBuyer, public StandardSeller {
    public:
        StandardFull(string userName);
};