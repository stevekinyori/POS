import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PosCOMPANY_DETAILSModule } from './company-details/company-details.module';
import { PosEmployeesModule } from './employees/employees.module';
import { PosProductCategoriesModule } from './product-categories/product-categories.module';
import { PosSubCategoriesModule } from './sub-categories/sub-categories.module';
import { PosPackagingModule } from './packaging/packaging.module';
import { PosBrandsModule } from './brands/brands.module';
import { PosProductsModule } from './products/products.module';
import { PosInventoryModule } from './inventory/inventory.module';
import { PosSuppliersModule } from './suppliers/suppliers.module';
import { PosSupplierProductsModule } from './supplier-products/supplier-products.module';
import { PosGoodsReceivedFromSupplierModule } from './goods-received-from-supplier/goods-received-from-supplier.module';
import { PosGoodsReturnedToSupplierModule } from './goods-returned-to-supplier/goods-returned-to-supplier.module';
import { PosSalesModule } from './sales/sales.module';
import { PosSaleTransactionsModule } from './sale-transactions/sale-transactions.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PosCOMPANY_DETAILSModule,
        PosEmployeesModule,
        PosProductCategoriesModule,
        PosSubCategoriesModule,
        PosPackagingModule,
        PosBrandsModule,
        PosProductsModule,
        PosInventoryModule,
        PosSuppliersModule,
        PosSupplierProductsModule,
        PosGoodsReceivedFromSupplierModule,
        PosGoodsReturnedToSupplierModule,
        PosSalesModule,
        PosSaleTransactionsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosEntityModule {}
