import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Products } from './products.model';
import { ProductsPopupService } from './products-popup.service';
import { ProductsService } from './products.service';
import { SubCategories, SubCategoriesService } from '../sub-categories';
import { Brands, BrandsService } from '../brands';
import { SupplierProducts, SupplierProductsService } from '../supplier-products';
import { GoodsReceivedFromSupplier, GoodsReceivedFromSupplierService } from '../goods-received-from-supplier';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-products-dialog',
    templateUrl: './products-dialog.component.html'
})
export class ProductsDialogComponent implements OnInit {

    products: Products;
    isSaving: boolean;

    subcategories: SubCategories[];

    brands: Brands[];

    supplierproducts: SupplierProducts[];

    goodsreceivedfromsuppliers: GoodsReceivedFromSupplier[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private productsService: ProductsService,
        private subCategoriesService: SubCategoriesService,
        private brandsService: BrandsService,
        private supplierProductsService: SupplierProductsService,
        private goodsReceivedFromSupplierService: GoodsReceivedFromSupplierService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.subCategoriesService
            .query({filter: 'subcategoryproducts(name)-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.products.subCategory || !this.products.subCategory.id) {
                    this.subcategories = res.json;
                } else {
                    this.subCategoriesService
                        .find(this.products.subCategory.id)
                        .subscribe((subRes: SubCategories) => {
                            this.subcategories = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.brandsService
            .query({filter: 'products(name)-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.products.brand || !this.products.brand.id) {
                    this.brands = res.json;
                } else {
                    this.brandsService
                        .find(this.products.brand.id)
                        .subscribe((subRes: Brands) => {
                            this.brands = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.supplierProductsService.query()
            .subscribe((res: ResponseWrapper) => { this.supplierproducts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.goodsReceivedFromSupplierService.query()
            .subscribe((res: ResponseWrapper) => { this.goodsreceivedfromsuppliers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.products.id !== undefined) {
            this.subscribeToSaveResponse(
                this.productsService.update(this.products));
        } else {
            this.subscribeToSaveResponse(
                this.productsService.create(this.products));
        }
    }

    private subscribeToSaveResponse(result: Observable<Products>) {
        result.subscribe((res: Products) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Products) {
        this.eventManager.broadcast({ name: 'productsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSubCategoriesById(index: number, item: SubCategories) {
        return item.id;
    }

    trackBrandsById(index: number, item: Brands) {
        return item.id;
    }

    trackSupplierProductsById(index: number, item: SupplierProducts) {
        return item.id;
    }

    trackGoodsReceivedFromSupplierById(index: number, item: GoodsReceivedFromSupplier) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-products-popup',
    template: ''
})
export class ProductsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private productsPopupService: ProductsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.productsPopupService
                    .open(ProductsDialogComponent as Component, params['id']);
            } else {
                this.productsPopupService
                    .open(ProductsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
