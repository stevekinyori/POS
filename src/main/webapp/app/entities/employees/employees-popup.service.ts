import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Employees } from './employees.model';
import { EmployeesService } from './employees.service';

@Injectable()
export class EmployeesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private employeesService: EmployeesService

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
                this.employeesService.find(id).subscribe((employees) => {
                    if (employees.dob) {
                        employees.dob = {
                            year: employees.dob.getFullYear(),
                            month: employees.dob.getMonth() + 1,
                            day: employees.dob.getDate()
                        };
                    }
                    this.ngbModalRef = this.employeesModalRef(component, employees);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.employeesModalRef(component, new Employees());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    employeesModalRef(component: Component, employees: Employees): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.employees = employees;
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
