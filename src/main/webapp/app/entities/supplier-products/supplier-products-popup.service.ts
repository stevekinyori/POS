import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SupplierProducts } from './supplier-products.model';
import { SupplierProductsService } from './supplier-products.service';

@Injectable()
export class SupplierProductsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private supplierProductsService: SupplierProductsService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.supplierProductsService.find(id).subscribe((supplierProducts) => {
                    if (supplierProducts.firstItemDeliveryDate) {
                        supplierProducts.firstItemDeliveryDate = {
                            year: supplierProducts.firstItemDeliveryDate.getFullYear(),
                            month: supplierProducts.firstItemDeliveryDate.getMonth() + 1,
                            day: supplierProducts.firstItemDeliveryDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.supplierProductsModalRef(component, supplierProducts);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.supplierProductsModalRef(component, new SupplierProducts());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    supplierProductsModalRef(component: Component, supplierProducts: SupplierProducts): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.supplierProducts = supplierProducts;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
